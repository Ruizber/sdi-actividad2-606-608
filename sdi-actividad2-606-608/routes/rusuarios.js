module.exports = function (app, swig, gestorBD) {
    app.get("/index", function (req, res) {
        let respuesta = swig.renderFile('views/index.html', {});
        res.send(respuesta);
        app.get("logger").info('El usuario accedió al index');
    });

    app.get('/desconectarse', function (req, res) {
        app.get("logger").info('El usuario ' + req.session.usuario.email + " ha cerrado sesión correctamente");
        req.session.usuario = undefined;
        res.redirect("/identificarse");
    });

    app.get("/registrarse", function (req, res) {
        let respuesta = swig.renderFile('views/bregistro.html', {});
        res.send(respuesta);
        app.get('logger').info('Usuario se va a registrar');
    });

    app.post('/usuario', function (req, res) {
        app.get('logger').info('Usuario se va a registrar');
        if (req.body.nombre === undefined || req.body.nombre === '' || req.body.apellidos === undefined || req.body.apellidos === ''
            || req.body.password === undefined || req.body.password === '' || req.body.repassword === undefined || req.body.repassword === ''
            || req.body.email === undefined || req.body.email === '') {
            res.redirect("/registrarse?mensaje=Es necesario completar todos los campos&tipoMensaje=alert-danger")
            app.get('logger').error('Es necesario completar todos los campos');
        } else if (req.body.password !== req.body.repassword) {
            res.redirect("/registrarse?mensaje=Las contraseñas deben ser iguales&tipoMensaje=alert-danger")
            app.get('logger').error('Las contraseñas deben ser iguales');
        } else {
            let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let usuario = {
                email: req.body.email,
                nombre: req.body.nombre,
                apellido: req.body.apellidos,
                dinero: 100,
                rol: 'usuario',
                password: seguro
            };
            let criterio = {
                email: req.body.email
            };
            gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                if (usuarios != null && usuarios.length !== 0) {
                    app.get("logger").error('El email ya está registrado');
                    res.redirect("/registrarse?mensaje=El email ya está registrado&tipoMensaje=alert-danger");
                } else {
                    gestorBD.insertarUsuario(usuario, function (id) {
                        if (id === undefined) {
                            app.get("logger").error('Error al registrar al usuarios');
                            res.redirect("/registrarse?mensaje=Error al registrar usuario&tipoMensaje=alert-danger")
                        } else {
                            req.session.usuario = usuario;
                            app.get("logger").info('Usuario registrado como ' + req.body.email);
                            delete req.session.usuario.password;
                            res.redirect("/publicaciones?mensaje=Nuevo usuario registrado");
                        }
                    })
                }
            })
        }
    });

    app.get("/identificarse", function (req, res) {
        let answer = swig.renderFile('views/bidentificacion.html', {});
        res.send(answer);
        app.get("logger").info('Usuario se intenta loguear');
    });

    app.post("/identificarse", function (req, res) {
        if (req.body.email === '' || req.body.email === undefined) {
            app.get("logger").error('El email no puede estar vacío');
            res.redirect("/identificarse?mensaje=El email no puede estar vacío")
        } else if (req.body.password === undefined || req.body.password === '') {
            app.get("logger").error('La contraseña no puede estar vacía');
            res.redirect("/identificarse?mensaje=La contraseña no puede estar vacía")
        } else {
            let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let criterio = {
                email: req.body.email,
                password: seguro
            };
            gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                    if (usuarios === undefined || usuarios.length === 0) {
                        req.session.usuario = undefined;
                        res.redirect("/identificarse" +
                            "?mensaje=Email o password incorrecto" +
                            "&tipoMensaje=alert-danger ");
                        app.get("logger").error('Email o password incorrecto');
                    } else {
                        req.session.usuario = usuarios[0];
                        app.get("logger").info('El usuario ' + req.session.usuario.email + " se ha logueado correctamente");
                        delete req.session.usuario.password;
                        if (usuarios[0].rol == 'admin')
                            res.redirect("/listarUsuarios");
                        else
                            res.redirect("/publicaciones");

                    }
                }
            );
        }
    });

    app.get('/listarUsuarios', function (req, res) {
        app.get("logger").info('El usuario ' + req.session.usuario.email + " lista los usuarios de la aplicación");
        let criterioMongo = {
            $and: [
                {
                    email: {
                        $ne: req.session.usuario.email
                    }
                },
                {
                    valid: {
                        $ne: false
                    }
                }
            ]
        };

        gestorBD.obtenerUsuarios(criterioMongo, function (usuarios) {
            if (usuarios !== null) {
                var respuesta = swig.renderFile('views/listarUsuarios.html', {
                    usuario: req.session.usuario,
                    usuarios: usuarios
                });
                res.send(respuesta);
                app.get("logger").info('Se han listado correctamente los usuarios de la aplicación');
            } else {
                res.redirect("/identificarse");
                app.get("logger").error('No se han podido listar los usuarios de la aplicación');
            }
        })
    });

    app.post('/delete', function (req, res) {
        let idsUsers = req.body.idsUser;
        if (idsUsers === undefined) {
            res.redirect("/listarUsuarios" +
                "?mensaje=Los usuarios no pudieron eliminarse" + "&tipoMensaje=alert-danger ");
        } else {
            if (!Array.isArray(idsUsers)) {
                let aux = idsUsers;
                idsUsers = [];
                idsUsers.push(aux);
            }
            let criterioConversacion = {
                $or: [{receptor: {$in: idsUsers}},
                    {emisor: {$in: idsUsers}}]
            };
            gestorBD.eliminarMensajes(criterioConversacion, function (conversaciones) {
                if (conversaciones == null) {
                    app.get("logger").error("No se pudieron borrar las conversaciones de los usuarios");
                    res.redirect("/listarUsuarios" +
                        "?mensaje=No se pudieron borrar las conversaciones de los usuarios");
                } else {
                    let criterioMensajes = {
                        $or: [{receptor: {$in: idsUsers}},
                            {emisor: {$in: idsUsers}}]
                    };
                    gestorBD.eliminarMensajes(criterioMensajes, function (mensajes) {
                        if (mensajes == null) {
                            app.get("logger").error("No se pudieron borrar los mensajes de los usuarios");
                            res.redirect("/listarUsuarios" +
                                "?mensaje=No se pudieron borrar los mensajes de los usuarios");
                        } else {
                            let criterioOferta = {
                                autor: {$in: idsUsers}
                            };
                            gestorBD.eliminarOferta(criterioOferta, function (ofertas) {
                                if (ofertas == null) {
                                    app.get("logger").error("No se pudieron borrar las ofertas de los usuarios");
                                    res.redirect("/listarUsuarios" +
                                        "?mensaje=No se pudieron borrar las ofertas de los usuarios");
                                } else {
                                    let criterio = {
                                        email: {$in: idsUsers}
                                    };
                                    let nuevoCriterio = {valid: false};
                                    gestorBD.eliminarUsuarios(criterio, nuevoCriterio, function (usuarios) {
                                        if (usuarios == null || usuarios.length === 0) {
                                            app.get("logger").error("Los usuarios no pudieron eliminarse");
                                            res.redirect("/listarUsuarios" +
                                                "?mensaje=Los usuarios no pudieron eliminarse");
                                        } else {
                                            app.get("logger").info("Los usuarios se eliminaron correctamente");
                                            res.redirect("/listarUsuarios" +
                                                "?mensaje=Los usuarios se eliminaron correctamente");
                                        }
                                    });
                                }
                            });
                        }
                    })
                }
            });
        }
    });
}
;
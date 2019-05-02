module.exports = function (app, swig, gestorBD) {
    app.get("/index", function (req, res) {
        let respuesta = swig.renderFile('views/index.html', {});
        res.send(respuesta);
    });

    app.get('/desconectarse', function (req, res) {
        req.session.usuario = undefined;
        res.redirect("/identificarse");
    });

    app.get("/registrarse", function (req, res) {
        let respuesta = swig.renderFile('views/bregistro.html', {});
        res.send(respuesta);
    });

    app.post('/usuario', function (req, res) {
        if (req.body.nombre === undefined || req.body.nombre === '' || req.body.apellidos === undefined || req.body.apellidos === ''
            || req.body.password === undefined || req.body.password === '' || req.body.repassword === undefined || req.body.repassword === ''
            || req.body.email === undefined || req.body.email === '') {
            res.redirect("/registrarse?mensaje=Es necesario completar todos los campos&tipoMensaje=alert-danger")
        } else if (req.body.password !== req.body.repassword) {
            res.redirect("/registrarse?mensaje=Las contraseñas deben ser iguales&tipoMensaje=alert-danger")
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
            gestorBD.insertarUsuario(usuario, function (id) {
                if (id === undefined) {
                    res.redirect("/registrarse?mensaje=Error al registrar usuario&tipoMensaje=alert-danger")
                } else {
                    res.redirect("/identificarse?mensaje=Nuevo usuario registrado");
                }
            });
        }
    });

    app.get("/identificarse", function (req, res) {
        let answer = swig.renderFile('views/bidentificacion.html', {});
        res.send(answer);
    });

    app.post("/identificarse", function (req, res) {
        if (req.body.email === '' || req.body.email === undefined) {
            res.redirect("/identificarse?mensaje=El email no puede estar vacío")
        } else if (req.body.password === undefined || req.body.password === '') {
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
                    } else {
                        req.session.usuario = usuarios[0];
                        if (usuarios[0].rol == 'admin')
                            res.redirect("/listarUsuarios");
                        else
                            res.redirect("/publicaciones");

                    }
                }
            );
        }
    });

    app.get("/registrarse", function (req, res) {
        let respuesta = swig.renderFile('views/bregistro.html', {});
        res.send(respuesta);
    });

    app.get('/listarUsuarios', function (req, res) {
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
            } else {
                res.redirect("/identificarse");
            }
        })
    });

    app.post('/delete', function (req, res) {
        let idsUsers = req.body.idsUsers;
        if (!Array.isArray(idsUsers)) {
            let aux = idsUsers;
            idsUsers = [];
            idsUsers.push(aux);
        }
        let criterio = {
            email: req.body.email
        };
        gestorBD.eliminarUsuarios(criterio, function (usuarios) {
            if (usuarios === undefined || usuarios.length === 0) {
                res.redirect("/listarUsuarios");
            } else {
                res.redirect("/listarUsuarios");
            }
        });
    });
}
;
module.exports = function (app, swig, gestorBD) {
    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        res.redirect("/identificarse");
    });

    app.get("/registrarse", function (req, res) {
        var respuesta = swig.renderFile('views/bregistro.html', {});
        res.send(respuesta);
    });

    app.post('/usuario', function (req, res) {
        if(req.body.nombre == null || req.body.nombre == '' || req.body.apellidos == null || req.body.apellidos == ''
        || req.body.password == null || req.body.password == '' || req.body.repassword == null || req.body.repassword == ''
        || req.body.email == null || req.body.email == '') {
            res.redirect("/registrarse?mensaje=Es necesario completar todos los campos&tipoMensaje=alert-danger")
        }
        else if(req.body.password != req.body.repassword) {
            res.redirect("/registrarse?mensaje=Las contraseñas deben ser iguales&tipoMensaje=alert-danger")
        }
        else {
            var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            var usuario = {
                email: req.body.email,
                nombre: req.body.nombre,
                password: seguro
            }
            gestorBD.insertarUsuario(usuario, function (id) {
                if (id == null) {
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
        if(req.body.email == '' || req.body.email == null) {
            res.redirect("/identificarse?mensaje=El email no puede estar vacío")
        }
        else if ( req.body.password == null || req.body.password == '') {
            res.redirect("/identificarse?mensaje=La contraseña no puede estar vacía")
        } else {
            var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            var criterio = {
                email: req.body.email,
                password: seguro
            }
            gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                if (usuarios == null || usuarios.length == 0) {
                    req.session.usuario = null;
                    res.redirect("/identificarse" +
                        "?mensaje=Email o password incorrecto" +
                        "&tipoMensaje=alert-danger ");
                } else {
                    req.session.usuario = usuarios[0].email;
                    res.redirect("/publicaciones");
                }
            });
        }
    });

    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        res.redirect("/identificarse?mensaje=Se ha cerrado sesión");
    });

};
module.exports = function (app, gestorBD) {

    app.get("/api/oferta", function (req, res) {
        gestorBD.obtenerOfertas({}, function (ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(ofertas));
            }
        });
    });

    app.get("/api/oferta/:id", function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)}

        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(ofertas[0]));
            }
        });
    });

    app.delete("/api/oferta/:id", function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)}

        gestorBD.eliminarOferta(criterio, function (oferta) {
            if (oferta == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(ofertas));
            }
        });
    });

    app.post("/api/oferta", function (req, res) {
        var oferta = {
            nombre: req.body.nombre,
            fecha: req.body.fecha,
            precio: req.body.precio,
        }
        gestorBD.insertarOferta(oferta, function (id) {
            if (id == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(201);
                res.json({
                    mensaje: "oferta insertarda",
                    _id: id
                })
            }
        });
    });

    app.put("/api/oferta/:id", function (req, res) {

        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};

        var oferta = {}; // Solo los atributos a modificar
        if (req.body.nombre != null)
            oferta.nombre = req.body.nombre;
        if (req.body.fecha != null)
            oferta.fecha = req.body.fecha;
        if (req.body.precio != null)
            oferta.precio = req.body.precio;
        gestorBD.modificarOferta(criterio, oferta, function (result) {
            if (result == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.json({
                    mensaje: "oferta modificada",
                    _id: req.params.id
                })
            }
        });
    });

    app.post("/api/autenticar/", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let criterio = {
            email: req.body.email,
            password: seguro
        };
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length === 0) {
                res.status(401); // Unauthorized
                res.json({
                    autenticado: false
                })
            } else {
                let token = app.get('jwt').sign(
                    {
                        usuario: criterio.email,
                        tiempo: Date.now() / 1000
                    },
                    "secreto");
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token
                })
            }

        });
    });

}
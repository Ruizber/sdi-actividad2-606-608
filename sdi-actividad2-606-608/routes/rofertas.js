module.exports = function (app, swig, gestorBD) {

    app.get("/ofertas", function (req, res) {
        let respuesta = "";
        if (req.query.nombre != null)
            respuesta += 'Nombre: ' + req.query.nombre + '<br>';
        if (typeof (req.query.autor) != "undefined")
            respuesta += 'Autor: ' + req.query.autor;
        res.send(respuesta);
    });

    app.get('/ofertas/:id', function (req, res) {
        let respuesta = 'id: ' + req.params.id;
        res.send(respuesta);
    });

    app.post("/oferta", function (req, res) {
        if (req.session.usuario == null) {
            res.redirect("/tienda");
            return;
        }
        let oferta = {
            nombre: req.body.nombre,
            precio: req.body.precio,
            autor: req.session.usuario
        }
        gestorBD.insertarOferta(oferta, function (id) {
            if (id == null) {
                res.send("Error al insertar oferta");
            } else {
                res.send("Agregada la oferta ID: " + id);
            }
        });
    });

    app.get("/tienda", function (req, res) {
        let criterio = {};
        if (req.query.busqueda != null) {
            criterio = {"nombre": {$regex: ".*" + req.query.busqueda + ".*"}};
        }
        let pg = parseInt(req.query.pg); // Es String !!!
        if (req.query.pg == null) { // Puede no venir el param
            pg = 1;
        }
        gestorBD.obtenerOfertasPg(criterio, pg, function (ofertas, total) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                let ultimaPg = total / 4;
                if (total % 4 > 0) { // Sobran decimales
                    ultimaPg = ultimaPg + 1;
                }
                let paginas = []; // paginas mostrar
                for (let i = pg - 2; i <= pg + 2; i++) {
                    if (i > 0 && i <= ultimaPg) {
                        paginas.push(i);
                    }
                }
                let respuesta = swig.renderFile('views/index.html',
                    {
                        // ofertas: ofertas,
                        // paginas: paginas,
                        // actual: pg
                    });
                res.send(respuesta);
            }
        });
    });

    app.get('/ofertas/agregar', function (req, res) {
        if (req.session.usuario == null) {
            res.redirect("/tienda");
            return;
        }
        let respuesta = swig.renderFile('views/bagregar.html', {});
        res.send(respuesta);
    });

    app.post('/oferta/modificar/:id', function (req, res) {
        let id = req.params.id;
        let criterio = {"_id": gestorBD.mongo.ObjectID(id)};
        let oferta = {
            nombre: req.body.nombre,
            precio: req.body.precio
        }
        gestorBD.modificarOferta(criterio, oferta, function (result) {
            if (result == null) {
                res.send("Error al modificar ");
            } else {
                res.send("Modificado " + result);
            }
        });
    });
    app.get('/oferta/eliminar/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.eliminarOferta(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send(respuesta);
            } else {
                res.redirect("/publicaciones");
            }
        });
    });
    app.get('/oferta/comprar/:id', function (req, res) {
        let ofertaId = gestorBD.mongo.ObjectID(req.params.id);
        let compra = {
            usuario: req.session.usuario,
            ofertaId: ofertaId
        }
        gestorBD.insertarCompra(compra, function (idCompra) {
            if (idCompra == null) {
                res.send(respuesta);
            } else {
                res.redirect("/compras");
            }
        });
    });
    app.get('/compras', function (req, res) {
        let criterio = {"usuario": req.session.usuario};
        gestorBD.obtenerCompras(criterio, function (compras) {
            if (compras == null) {
                res.send("Error al listar ");
            } else {
                let ofertasCompradasIds = [];
                for (i = 0; i < compras.length; i++) {
                    ofertasCompradasIds.push(compras[i].ofertaId);
                }
                let criterio = {"_id": {$in: ofertasCompradasIds}}
                gestorBD.obtenerOfertas(criterio, function (ofertas) {
                    let respuesta = swig.renderFile('views/bcompras.html',
                        {
                            ofertas: ofertas
                        });
                    res.send(respuesta);
                });
            }
        });
    });

};
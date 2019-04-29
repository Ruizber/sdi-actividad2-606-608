module.exports = function (app, swig, gestorBD) {
    app.get('/ofertas/agregar', function (req, res) {
        if (req.session.usuario === null) {
            res.redirect("/tienda");
            return;
        }
        var respuesta = swig.renderFile('views/bagregar.html', {});
        res.send(respuesta);
    });
    app.get('/oferta/:id', function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas === null) {
                res.send(respuesta);
            } else {
                var respuesta = swig.renderFile('views/boferta.html',
                    {
                        oferta: ofertas[0],
                        usuario: req.session.usuario,
                    });
                res.send(respuesta);
            }
        });
    });

    app.post("/oferta", function (req, res) {
        if (req.session.usuario === null) {
            res.redirect("/tienda");
            return;
        }
        let oferta = {
            nombre: req.body.nombre,
            detalle: req.body.detalle,
            fecha: req.body.fecha,
            precio: req.body.precio,
            autor: req.session.usuario.email
        };
        gestorBD.insertarOferta(oferta, function (id) {
            if (id === null) {
                res.send("Error al insertar oferta");
            } else {
                res.redirect("/publicaciones");
            }
        });
    });

    app.get("/tienda", function (req, res) {
        let criterio = {
                autor: {
                     $ne: req.session.usuario.email
            }};
        if (req.query.busqueda !== undefined) {
            criterio = {
                "nombre": {
                    $regex: ".*" + req.query.busqueda + ".*", $options: "i"
                }
            };
        }
        let pg = parseInt(req.query.pg); // Es String !!!
        if (req.query.pg === null) { // Puede no venir el param
            pg = 1;
        }
        gestorBD.obtenerOfertasPg(criterio, pg, function (ofertas, total) {
            if (ofertas === null) {
                res.send("Error al listar ");
            } else {
                let ultimaPg = total / 5;
                if (total % 5 > 0) { // Sobran decimales
                    ultimaPg = ultimaPg + 1;
                }
                let paginas = []; // paginas mostrar
                for (let i = pg - 2; i <= pg + 2; i++) {
                    if (i > 0 && i <= ultimaPg) {
                        paginas.push(i);
                    }
                }
                let respuesta = swig.renderFile('views/tienda.html',
                    {
                        usuario: req.session.usuario,
                        ofertas: ofertas,
                        paginas: paginas,
                        actual: pg
                    });
                res.send(respuesta);
            }
        });
    });
    app.get('/oferta/eliminar/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.eliminarOferta(criterio, function (ofertas) {
            if (ofertas === null) {
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
            if (idCompra === null) {
                res.send(respuesta);
            } else {
                res.redirect("/compras");
            }
        });
    });
    app.get('/compras', function (req, res) {
        let criterio = {"usuario": req.session.usuario};
        gestorBD.obtenerCompras(criterio, function (compras) {
            if (compras === null) {
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
                            usuario: req.session.usuario,
                            ofertas: compras
                        });
                    res.send(respuesta);
                });
            }
        });
    });

    app.get("/publicaciones", function (req, res) {
        var criterio = {autor: req.session.usuario.email};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas === null) {
                res.send("Error al listar ");
            } else {
                console.log(req.session.usuario);
                var respuesta = swig.renderFile('views/bpublicaciones.html',
                    {
                        usuario: req.session.usuario,
                        ofertas: ofertas
                    });
                res.send(respuesta);
            }
        });
    });

};
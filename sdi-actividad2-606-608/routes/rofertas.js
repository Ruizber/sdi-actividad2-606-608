module.exports = function (app, swig, gestorBD) {
    app.get('/oferta/destacar/:id', function (req, res) {
        gestorBD.obtenerOfertas({_id: req.params.id}, function (ofe) {
            if (req.params.id === null || req.params.id === undefined || req.params.id === '') {
                res.redirect("/publicaciones?mensaje=El valor de la oferta no es válido&tipoMensaje=alert-danger ");
            } else {
                var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
                if (req.session.usuario.dinero < 20) {
                    res.redirect("/publicaciones?mensaje=No tienes suficiente dinero, necesitas 20€" +
                        "&tipoMensaje=alert-danger ");
                } else {
                    gestorBD.destacarOferta(criterio, function (ofertas) {
                        if (ofertas == null) {
                            res.redirect("/publicaciones?mensaje=No se puede destacar esta publicacion" +
                                "&tipoMensaje=alert-danger ");
                        } else {
                            gestorBD.usuarioDestaca({"email": req.session.usuario.email}, function (result) {
                                if (result == null) {
                                    res.redirect("/publicaciones?mensaje=Error destacando la oferta" +
                                        "&tipoMensaje=alert-danger ");
                                } else {
                                    req.session.usuario.dinero -= 20;
                                    res.redirect("/publicaciones?mensaje=Publicacion destacada&tipoMensaje=alert-success ");
                                }
                            });
                        }
                    });
                }
            }
        });
    });

    app.get("/oferta/compradas", function (req, res) {
        var criterio = {
            buyer: req.session.usuario.email,
            valid: true
        };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            var respuesta = swig.renderFile('/bcompras.html', {salesList: ofertas});
            res.send(respuesta);
        });
    });

    app.get('/oferta/comprar/:id', function (req, res) {
        var criterio = {_id: gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null || ofertas.length == 0) {
                res.redirect("/tienda?mensaje=La compra no pudo completarse");
            } else {
                if (ofertas[0].precio <= req.session.usuario.dinero) {
                    var nuevoCriterio = {
                        onsale: false,
                        comprador: req.session.usuario.email
                    };
                    var criterioComprador = {
                        email: req.session.usuario.email
                    };
                    var nuevoCriterioComprador = {
                        dinero: req.session.usuario.dinero - ofertas[0].precio
                    };
                    gestorBD.comprarOferta(criterio, nuevoCriterio, function (ofertas) {
                        if (ofertas == null || ofertas.length == 0) {
                            res.redirect("/tienda?mensaje=La oferta no pudo comprarse correctamente");
                        } else {
                            gestorBD.actualizarDinero(criterioComprador, nuevoCriterioComprador, function (usuarios) {
                                if (usuarios == null || usuarios.length == 0) {
                                    res.redirect("/tienda?mensaje=La oferta no pudo comprarse correctamente");
                                } else {
                                    res.redirect("/compras?mensaje=La oferta se compró correctamente");
                                }
                            })
                        }
                    });
                } else {
                    res.redirect("/tienda?mensaje=No tienes suficiente dinero para adquirir esa oferta");
                }
            }
        })
    });

    app.get('/compras', function (req, res) {
        var criterio = {"comprador": req.session.usuario.email};
        gestorBD.obtenerOfertas(criterio, function (compras) {
            if (compras == null) {
                res.send("Error al listar ");
            } else {
                var respuesta = swig.renderFile('views/bcompras.html',
                    {
                        usuario: req.session.usuario,
                        ofertas: compras
                    });
                res.send(respuesta);
            }
        });
    });

    app.get('/ofertas/agregar', function (req, res) {
        if (req.session.usuario === null) {
            res.redirect("/tienda");
            return;
        }
        var respuesta = swig.renderFile('views/bagregar.html', {});
        res.send(respuesta);
    });

    app.get('/oferta/eliminar/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.eliminarOferta(criterio, function (ofertas) {
            if (ofertas === null) {
                res.redirect("/publicaciones" +
                    "?mensaje=Error al eliminar la oferta" +
                    "&tipoMensaje=alert-danger ");
            } else {
                res.redirect("/publicaciones" +
                    "?mensaje=La oferta se elimino correctamente");
            }
        });
    });

    app.get('/oferta/:id', function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas === null) {
                res.redirect("/tienda?mensaje=No se ha encontrado la oferta");
            } else {
                var respuesta = swig.renderFile('views/boferta.html',
                    {
                        oferta: ofertas[0],
                        usuario: req.session.usuario
                    });
                res.send(respuesta);
            }
        });
    });

    app.post("/oferta", function (req, res) {
            let today = new Date();
            let dd = String(today.getDate()).padStart(2, '0');
            let mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
            let yyyy = today.getFullYear();
            let dateString = mm + '/' + dd + '/' + yyyy;
            let oferta = {
                nombre: req.body.nombre,
                detalles: req.body.detalles,
                precio: req.body.precio,
                destacada: (req.body.destacada === "on"),
                propietario: req.session.usuario.email,
                fecha: dateString
            };
            if (oferta.destacada && req.session.usuario.dinero < 20) {
                res.redirect("/ofertas/agregar?mensaje=No tienes suficiente dinero para destacar la oferta");
            } else if (oferta.nombre == null || oferta.nombre == '' || oferta.detalles == null ||
                oferta.detalles == '' || oferta.precio == null || oferta.precio <= 0) {
                res.redirect("/ofertas/agregar?mensaje=Los campos no son validos");
            } else if (isNaN(oferta.precio)) {
                res.redirect("/ofertas/agregar?mensaje=El valor del precio debe ser numérico");
            } else {
                gestorBD.insertarOferta(oferta, function (id) {
                    if (id == null) {
                        res.redirect("/publicaciones?mensaje=Error al añadir oferta");
                    } else {
                        if (oferta.destacada) {
                            gestorBD.usuarioDestaca({"email": req.session.usuario.email}, function (result) {
                                if (result == null) {
                                    res.redirect("/publicaciones?mensaje=Error destacando la oferta" +
                                        "&tipoMensaje=alert-danger ");
                                } else {
                                    req.session.usuario.dinero -= 20;
                                    res.redirect("/publicaciones?mensaje=Nueva oferta añadida");
                                }
                            });
                        } else {
                            res.redirect("/publicaciones?mensaje=Nueva oferta añadida");
                        }
                    }
                });
            }
        }
    );

    app.get("/tienda", function (req, res) {
        let criterio = {
            autor: {
                $ne: req.session.usuario.email
            }
        };
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


    app.get('/oferta/comprar/:id', function (req, res) {
        let ofertaId = gestorBD.mongo.ObjectID(req.params.id);
        let compra = {
            usuario: req.session.usuario,
            ofertaId: ofertaId
        };

        gestorBD.insertarCompra(compra, function (idCompra) {
            if (idCompra === null) {
                res.redirect("/tienda?error=Fallo al comprar");
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
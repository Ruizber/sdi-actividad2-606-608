module.exports = function (app, gestorBD) {
    app.get("/api/oferta/", function (req, res) {
        let criterio = {
            autor: {
                $ne: res.usuario
            }
        };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
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

    app.delete("/api/oferta/:id", function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
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
        let oferta = {
            nombre: req.body.nombre,
            fecha: req.body.fecha,
            precio: req.body.precio,
        };
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

        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};

        let oferta = {}; // Solo los atributos a modificar
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

    app.post("/api/mensaje/oferta/:id", function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error: "Se ha producido un error"
                })
            } else {
                let oferta2 = ofertas[0];
                let criterio = {
                    $or: [
                        {
                            $and: [
                                {usuario: res.usuario},
                                {autor: oferta2.autor},
                                {oferta: gestorBD.mongo.ObjectID(req.params.id)}
                            ]
                        },
                        {
                            $and: [
                                {usuario: oferta2.autor},
                                {autor: res.usuario},
                                {oferta: gestorBD.mongo.ObjectID(req.params.id)}
                            ]
                        }
                    ]
                };
                gestorBD.obtenerConversacion(criterio, function (conversaciones) {
                    if (conversaciones == null) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error al buscar la conversación"
                        })
                    } else {
                        if (conversaciones.length > 0) {
                            let conversacion = conversaciones[0];
                            let mensaje = {
                                emisor: res.usuario,
                                receptor: req.body.receptor,
                                oferta: oferta2,
                                fecha: new Date(),
                                leido: false,
                                mensaje: req.body.mensaje,
                                conversacion: conversacion._id
                            };
                            gestorBD.insertarMensaje(mensaje, function (mensajes) {
                                if (mensajes == null) {
                                    res.status(500);
                                    res.json({
                                        error: "se ha producido un error"
                                    })
                                } else {
                                    res.status(200);
                                    res.send(JSON.stringify(mensajes));
                                }
                            });
                        } else {
                            let conversacion = {
                                oferta: oferta2._id,
                                autor: oferta2.autor,
                                usuario: res.usuario,
                                numMessage: 0
                            };
                            gestorBD.crearConversacion(conversacion, function (conversacion) {
                                if (conversaciones == null) {
                                    res.status(500);
                                    res.json({
                                        error: "se ha producido un error al buscar la conversación"
                                    });
                                } else {
                                    let oferta = ofertas[0];
                                    let mensaje = {
                                        emisor: res.usuario,
                                        receptor: req.body.receptor,
                                        oferta: oferta,
                                        fecha: new Date(),
                                        leido: false,
                                        mensaje: req.body.mensaje,
                                        conversacion: conversacion._id
                                    };
                                    gestorBD.insertarMensaje(mensaje, function (mensajes) {
                                        if (mensajes == null) {
                                            res.status(500);
                                            res.json({
                                                error: "se ha producido un error"
                                            })
                                        } else {
                                            res.status(200);
                                            res.send(JSON.stringify(mensajes));
                                        }
                                    });
                                }
                            })
                        }
                    }
                });
            }
        })
    });

    app.get("/api/mensaje/leido/:id", function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.pasarMensajeLeido(criterio, function (mensajes) {
            if (mensajes == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(mensajes));
            }
        })
    });

    app.get("/api/conversacion/:id", function (req, res) {
        let criterioMongo = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerConversacion(criterioMongo, function (conversaciones) {
            if (conversaciones == null || conversaciones.length === 0) {
                res.status(500);
                res.json({
                    error: "se ha producido un error con la conver"
                })
            } else {
                let conversacion = conversaciones[0];
                let criterio = {
                    conversacion: gestorBD.mongo.ObjectID(conversacion._id)
                };
                gestorBD.obtenerMensajes(criterio, function (mensajes) {
                    if (mensajes == null) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        })
                    } else {
                        res.status(200);
                        res.send(JSON.stringify(mensajes));
                    }
                });
            }
        });
    });

    app.get("/api/conversacion/eliminar/:id", function (req, res) {
        let criterioMongo = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerConversacion(criterioMongo, function (conversaciones) {
            if (conversaciones == null || conversaciones.length === 0) {
                res.status(500);
                res.json({
                    error: "se ha producido un error con la conver"
                })
            } else {
                let conversacion = conversaciones[0];
                let criterio = {
                    conversacion: gestorBD.mongo.ObjectID(conversacion._id)
                };
                gestorBD.eliminarMensajes(criterio, function (mensajes) {
                    if (mensajes == null) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        })
                    } else {
                        res.status(200);
                        res.send(JSON.stringify(mensajes));
                    }
                });
            }
        });
    });
};
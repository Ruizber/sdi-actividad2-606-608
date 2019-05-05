module.exports = function (app, gestorBD) {
    app.get("/api/oferta/", function (req, res) {
        let criterio = {
            autor: {
                $ne: res.usuario
            }
        };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                app.get("logger").error("Se ha producido un error al obtener las ofertas (API)");
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                app.get("logger").info("Las ofertas se listaron correctamente (API)");
                res.status(200);
                res.send(JSON.stringify(ofertas));
            }
        });
    });

    app.delete("/api/oferta/:id", function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.eliminarOferta(criterio, function (oferta) {
            if (oferta == null) {
                app.get("logger").error("Se ha producido un error al eliminar la oferta (API)");
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                app.get("logger").info("La oferta se eliminó correctamente (API)");
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
                app.get("logger").error("Se ha producido un error al insertar la oferta (API)");
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                app.get("logger").info("La oferta se insertó correctamente (API)");
                res.status(201);
                res.json({
                    mensaje: "oferta insertarda",
                    _id: id
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
                app.get("logger").error("Se ha producido un error al obtener el usuario (API)");
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
                app.get("logger").info("El usuario se ha autenticado correctamente (API)");
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
                app.get("logger").error("Se ha producido un error al obtener las ofertas (API)");
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
                        app.get("logger").error("Se ha producido un error al buscar la conversación (API)");
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
                                    app.get("logger").error("Se ha producido un error al insertar el mensaje (API)");
                                    res.status(500);
                                    res.json({
                                        error: "se ha producido un error"
                                    })
                                } else {
                                    app.get("logger").info("Se ha insertado correctamente el mensaje (API)");
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
                                    app.get("logger").error("Se ha producido un error al buscar la conversación (API)");
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
                                            app.get("logger").error("Se ha producido un error al insertar el mensaje (API)");
                                            res.status(500);
                                            res.json({
                                                error: "se ha producido un error"
                                            })
                                        } else {
                                            app.get("logger").info("Se ha insertado correctamente el mensaje (API)");
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
                app.get("logger").error("Se ha producido un error al pasar a leido el mensaje (API)");
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                app.get("logger").info("Se ha marcado como leido correctamente el mensaje (API)");
                res.status(200);
                res.send(JSON.stringify(mensajes));
            }
        })
    });

    app.get("/api/conversacion/:id", function (req, res) {
        let criterioMongo = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerConversacion(criterioMongo, function (conversaciones) {
            if (conversaciones == null || conversaciones.length === 0) {
                app.get("logger").error("Se ha producido un error al obtener la conversacion (API)");
                res.status(500);
                res.json({
                    error: "se ha producido un error con la conversacion"
                })
            } else {
                let conversacion = conversaciones[0];
                let criterio = {
                    conversacion: gestorBD.mongo.ObjectID(conversacion._id)
                };
                gestorBD.obtenerMensajes(criterio, function (mensajes) {
                    if (mensajes == null) {
                        app.get("logger").error("Se ha producido un error al obtener los mensajes (API)");
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        })
                    } else {
                        app.get("logger").info("Se han obtenido correctamente los mensajes (API)");
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
                app.get("logger").error("Se ha producido un error al obtener la conversacion (API)");
                res.status(500);
                res.json({
                    error: "se ha producido un error con la conversacion"
                })
            } else {
                let conversacion = conversaciones[0];
                let criterio = {
                    conversacion: gestorBD.mongo.ObjectID(conversacion._id)
                };
                gestorBD.eliminarMensajes(criterio, function (mensajes) {
                    if (mensajes == null) {
                        app.get("logger").error("Se ha producido un error al eliminar los mensajes (API)");
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        })
                    } else {
                        app.get("logger").info("Se han eliminado correctamente los mensajes (API)");
                        res.status(200);
                        res.send(JSON.stringify(mensajes));
                    }
                });
            }
        });
    });

    app.get("/api/search/conversacion/:ofertaId", function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.ofertaId)};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                app.get("logger").error("Se ha producido un error al obtener las ofertas (API)");
                res.status(500);
                res.json({
                    error: "Se ha producido un error"
                })
            } else {
                let oferta = ofertas[0];
                let criterio = {
                    $or: [
                        {
                            $and: [
                                {usuario: res.usuario},
                                {autor: oferta.autor},
                                {oferta: gestorBD.mongo.ObjectID(req.params.ofertaId)}
                            ]
                        },
                        {
                            $and: [
                                {usuario: oferta.autor},
                                {autor: res.usuario},
                                {oferta: gestorBD.mongo.ObjectID(req.params.ofertaId)}
                            ]
                        }
                    ]
                };
                gestorBD.obtenerConversacion(criterio, function (conversaciones) {
                    if (conversaciones == null) {
                        app.get("logger").error("Se ha producido un error al buscar la conversacion (API)");
                        res.status(500);
                        res.json({
                            error: "se ha producido un error al buscar la conversación"
                        })
                    } else {
                        app.get("logger").info("Se han obtenido correctamente las conversaciones (API)");
                        res.status(200);
                        res.send(JSON.stringify(conversaciones));
                    }
                });
            }
        });
    });
};
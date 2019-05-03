module.exports = {
    mongo: null,
    app: null,
    init: function (app, mongo) {
        this.mongo = mongo;
        this.app = app;
    },
    restarDinero: function (ofertaId, email, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('ofertas');
                var criterio = {"_id": ofertaId};
                collection.find(criterio).toArray(function (err, ofertas) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        var precioOferta = ofertas[0].precio;
                        collection = db.collection('usuarios');
                        criterio = {"email": email};
                        collection.find(criterio).toArray(function (err, usuarios) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                var dineroUsuario = usuarios[0].dinero;
                                console.log("Dinero del usuario: " + dineroUsuario);
                                console.log("Dinero del oferta: " + precioOferta);
                                if (dineroUsuario == null || precioOferta == null) {
                                    funcionCallback(null);
                                } else {
                                    var dineroFinal = dineroUsuario - precioOferta;
                                    if (dineroFinal < 0) {
                                        funcionCallback(dineroFinal);
                                    } else {
                                        collection.update(criterio, {$set: {"dinero": dineroFinal}}, function (err, result) {
                                            if (err) {
                                                funcionCallback(null);
                                            } else {
                                                funcionCallback(dineroFinal);
                                            }
                                            db.close();
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    ,
    sumarDinero: function (ofertaId, email, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('ofertas');
                var criterio = {"_id": ofertaId};
                collection.find(criterio).toArray(function (err, ofertas) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        var precioOferta = ofertas[0].precio;
                        collection = db.collection('usuarios');
                        criterio = {"email": email};
                        collection.find(criterio).toArray(function (err, usuarios) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                var dineroUsuario = usuarios[0].dinero;
                                if (dineroUsuario == null || precioOferta == null) {
                                    funcionCallback(null);
                                } else {
                                    var dineroFinal = Number.parseFloat(dineroUsuario) + Number.parseFloat(precioOferta);
                                    if (dineroFinal < 0) {
                                        funcionCallback(dineroFinal);
                                    } else {
                                        collection.update(criterio, {$set: {"dinero": dineroFinal}}, function (err, result) {
                                            if (err) {
                                                funcionCallback(null);
                                            } else {
                                                funcionCallback(dineroFinal);
                                            }
                                            db.close();
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    ,
    insertarOferta: function (oferta, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('ofertas');
                collection.insert(oferta, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]._id);
                    }
                    db.close();
                });
            }
        });
    },
    eliminarUsuarios: function (usuarios, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('usuarios');
                collection.removeMany(usuarios, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result);
                    }
                    db.close();
                });
            }
        });
    },
    obtenerUsuarios: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('usuarios');
                collection.find(criterio).toArray(function (err, usuarios) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(usuarios);
                    }
                    db.close();
                });
            }
        });
    },
    insertarUsuario: function (usuario, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('usuarios');
                collection.insert(usuario, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]._id);
                    }
                    db.close();
                });
            }
        });
    },
    insertarOferta: function (cancion, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('ofertas');
                collection.insert(cancion, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]._id);
                    }
                    db.close();
                });
            }
        });
    },
    obtenerOfertas: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('ofertas');
                collection.find(criterio).toArray(function (err, ofertas) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(ofertas);
                    }
                    db.close();
                });
            }
        });
    },
    eliminarOferta: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('ofertas');
                collection.remove(criterio, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result);
                    }
                    db.close();
                });
            }
        });
    },
    insertarCompra: function (compra, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('compras');
                collection.insert(compra, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]._id);
                    }
                    db.close();
                });
            }
        });
    },
    obtenerOfertasPg: function (criterio, pg, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('ofertas');
                collection.count(criterio, function (err, count) {
                    collection.find(criterio).skip((pg - 1) * 5).limit(5)
                        .toArray(function (err, ofertas) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                funcionCallback(ofertas, count);
                            }
                            db.close();
                        });
                });
            }
        });
    },

    insertarMensaje: function (mensaje, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('mensajes');
                collection.insert(mensaje, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]);
                        console.log("Se ha añadido el mensaje")
                    }
                    db.close();
                });
            }
        });
    },
    pasarMensajeLeido: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('mensajes');
                collection.update(criterio, {$set: {leido: true}}, function (err, resultado) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(resultado);
                    }
                });
            }
            db.close();
        });
    },

    obtenerCompras: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('compras');
                collection.find(criterio).toArray(function (err, usuarios) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(usuarios);
                    }
                    db.close();
                });
            }
        });
    },
    obtenerMensajes: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('mensajes');
                collection.find(criterio).toArray(function (err, canciones) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(canciones);
                    }
                    db.close();
                });
            }
        });
    },

    eliminarMensajes: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                var collection = db.collection('mensajes');
                collection.remove(criterio, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result);
                    }
                    db.close();
                });
            }
        });
    }
};
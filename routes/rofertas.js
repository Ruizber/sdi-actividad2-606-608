module.exports = function(app, swig, gestorBD) {

    app.get("/ofertas", function(req, res) {
        var respuesta = "";
        if (req.query.nombre != null)
            respuesta += 'Nombre: ' + req.query.nombre + '<br>';
        if (typeof (req.query.autor) != "undefined")
            respuesta += 'Autor: ' + req.query.autor;
        res.send(respuesta);
    });

    app.get('/ofertas/:id', function(req, res) {
        var respuesta = 'id: ' + req.params.id;
        res.send(respuesta);
    });

    app.post("/oferta", function(req, res) {
        if ( req.session.usuario == null){
            res.redirect("/tienda");
            return;
        }
        var oferta = {
            nombre : req.body.nombre,
            precio : req.body.precio,
            autor: req.session.usuario
        }
        gestorBD.insertarOferta(oferta, function(id){
            if (id == null) {
                res.send("Error al insertar oferta");
            } else {
                res.send("Agregada la oferta ID: " + id);
            }
        });
    });

    app.get("/tienda", function(req, res) {
        var criterio = {};
        if( req.query.busqueda != null ){
            criterio = { "nombre" : {$regex : ".*"+req.query.busqueda+".*"} };
        }
        var pg = parseInt(req.query.pg); // Es String !!!
        if ( req.query.pg == null){ // Puede no venir el param
            pg = 1;
        }
        gestorBD.obtenerOfertasPg(criterio, pg , function(ofertas, total ) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                var ultimaPg = total/4;
                if (total % 4 > 0 ){ // Sobran decimales
                    ultimaPg = ultimaPg+1;
                }
                var paginas = []; // paginas mostrar
                for(var i = pg-2 ; i <= pg+2 ; i++){
                    if ( i > 0 && i <= ultimaPg){
                        paginas.push(i);
                    }
                }
                var respuesta = swig.renderFile('views/btienda.html',
                    {
                        ofertas : ofertas,
                        paginas : paginas,
                        actual : pg
                    });
                res.send(respuesta);
            }
        });
    });

    app.get('/ofertas/agregar', function (req, res) {
        if ( req.session.usuario == null){
            res.redirect("/tienda");
            return;
        }
        var respuesta = swig.renderFile('views/bagregar.html', {});
        res.send(respuesta);
    });

    app.post('/oferta/modificar/:id', function (req, res) {
        var id = req.params.id;
        var criterio = { "_id" : gestorBD.mongo.ObjectID(id) };
        var oferta = {
            nombre : req.body.nombre,
            precio : req.body.precio
        }
        gestorBD.modificarOferta(criterio, oferta, function(result) {
            if (result == null) {
                res.send("Error al modificar ");
            } else {
                res.send("Modificado "+result);
            }
        });
    });
    app.get('/oferta/eliminar/:id', function (req, res) {
        var criterio = {"_id" : gestorBD.mongo.ObjectID(req.params.id) };
        gestorBD.eliminarOferta(criterio,function(ofertas){
            if ( ofertas == null ){
                res.send(respuesta);
            } else {
                res.redirect("/publicaciones");
            }
        });
    });
    app.get('/oferta/comprar/:id', function (req, res) {
        var ofertaId = gestorBD.mongo.ObjectID(req.params.id);
        var compra = {
            usuario : req.session.usuario,
            ofertaId : ofertaId
        }
        gestorBD.insertarCompra(compra ,function(idCompra){
            if ( idCompra == null ){
                res.send(respuesta);
            } else {
                res.redirect("/compras");
            }
        });
    });
    app.get('/compras', function (req, res) {
        var criterio = { "usuario" : req.session.usuario };
        gestorBD.obtenerCompras(criterio ,function(compras){
            if (compras == null) {
                res.send("Error al listar ");
            } else {
                var ofertasCompradasIds = [];
                for(i=0; i < compras.length; i++){
                    ofertasCompradasIds.push( compras[i].ofertaId );
                }
                var criterio = { "_id" : { $in: ofertasCompradasIds } }
                gestorBD.obtenerOfertas(criterio ,function(ofertas){
                    var respuesta = swig.renderFile('views/bcompras.html',
                        {
                            ofertas : ofertas
                        });
                    res.send(respuesta);
                });
            }
        });
    });

};
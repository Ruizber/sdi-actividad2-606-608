module.exports = function(app, swig) {

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
        res.send("Oferta agregada:"+req.body.nombre +"<br>"
            +" precio: "+req.body.precio);
    });
};
module.exports = function(app, swig) {
    app.get("/usuarios", function(req, res) {
        res.send("ver usuarios");
    });

    app.get("/asd", function (req, res) {
        let respuesta = swig.renderFile('views/templates/base.html', {});
        res.send(respuesta);
    });
};
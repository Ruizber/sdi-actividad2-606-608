let express = require('express');
let app = express();
let crypto = require('crypto');


let expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

let fileUpload = require('express-fileupload');
app.use(fileUpload());
let mongo = require('mongodb');
let swig = require('swig-templates');
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

let gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app, mongo);


// routerUsuarioSession
let routerUsuarioSession = express.Router();
routerUsuarioSession.use(function (req, res, next) {
    console.log("routerUsuarioSession");
    if (req.session.usuario) {
        // dejamos correr la petición
        next();
    } else {
        console.log("va a : " + req.session.destino)
        res.redirect("/identificarse");
    }
});
//Aplicar routerUsuarioSession
app.use("/ofertas/agregar", routerUsuarioSession);
app.use("/ofertas", routerUsuarioSession);
app.use("/oferta/comprar", routerUsuarioSession);
app.use("/ofertas", routerUsuarioSession);
app.use("/publicaciones", routerUsuarioSession);
app.use("/tienda", routerUsuarioSession);
app.use("/compras", routerUsuarioSession);

//routerUsuarioAutor
let routerUsuario = express.Router();
routerUsuario.use(function (req, res, next) {
    let path = require('path');
    let id = path.basename(req.originalUrl);
// Cuidado porque req.params no funciona
// en el router si los params van en la URL.
    gestorBD.obtenerUsuarios(
        {_id: mongo.ObjectID(id)}, function (usuarios) {
            if (usuarios[0].rol === 'usuario') {
                next();
            } else {
                res.redirect("/identificarse");
            }
        })
});
//Aplicar routerUsuarioAutor
app.use("/oferta/modificar", routerUsuario);
app.use("/oferta/eliminar", routerUsuario);

app.use(express.static('public'));
app.set('port', 8081);
app.set('db', 'mongodb://uo257947:uo257947@sdi-actividad2-606-608-shard-00-00-na9yb.mongodb.net:27017,sdi-actividad2-606-608-shard-00-01-na9yb.mongodb.net:27017,sdi-actividad2-606-608-shard-00-02-na9yb.mongodb.net:27017/test?ssl=true&replicaSet=sdi-actividad2-606-608-shard-0&authSource=admin&retryWrites=true');
app.set('clave', 'abcdefg');
app.set('crypto', crypto);


require("./routes/rusuarios.js")(app, swig, gestorBD);
require("./routes/rofertas.js")(app, swig, gestorBD);


app.use(function (err, req, res, next) {
    console.log("Error producido: " + err);
    if (!res.headersSent) {
        res.status(400);
        res.send("Recurso no disponible");
    }
});
app.get('/', function (req, res) {
    res.redirect('/index');
})

app.listen(app.get('port'), function () {
    console.log("Servidor activo");
});
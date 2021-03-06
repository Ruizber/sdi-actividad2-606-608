let express = require('express');
let app = express();
let crypto = require('crypto');
let jwt = require('jsonwebtoken');
app.set('jwt', jwt);


let expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));
app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
    // Debemos especificar todas las headers que se aceptan. Content-Type , token
    next();
});
let fileUpload = require('express-fileupload');
app.use(fileUpload());
let mongo = require('mongodb');
let swig = require('swig-templates');
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

let gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app, mongo);

// logger
let log4js = require('log4js');
log4js.configure({
    appenders: {sdi: {type: 'file', filename: 'logs/sdi.log'}},
    categories: {default: {appenders: ['sdi'], level: 'trace'}}
});
let logger = log4js.getLogger('sdi');


// routerUsuarioToken
var routerUsuarioToken = express.Router();
routerUsuarioToken.use(function (req, res, next) {
    // obtener el token, vía headers (opcionalmente GET y/o POST).
    var token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', function (err, infoToken) {
            if (err || (Date.now() / 1000 - infoToken.tiempo) > 24000) {
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe

            } else {
                // dejamos correr la petición
                res.usuario = infoToken.usuario;
                next();
            }
        });
    } else {
        res.status(403); // Forbidden
        res.json({
            acceso: false,
            mensaje: 'No hay Token'
        });
    }
});

// Aplicar routerUsuarioToken
app.use('/api/oferta', routerUsuarioToken);
app.use('/api/mensaje/oferta/:id', routerUsuarioToken);
app.use('/api/mensaje/leido/:id', routerUsuarioToken);
app.use('/api/search/conversacion/:ofertaId', routerUsuarioToken);
app.use('/api/conversacion/:id', routerUsuarioToken);
app.use('/api/conversacion/eliminar/:id', routerUsuarioToken);

// routerUsuarioSession
let routerUsuarioSession = express.Router();
routerUsuarioSession.use(function (req, res, next) {
    if (req.session.usuario) {
        // dejamos correr la petición
        next();
    } else {
        res.redirect("/identificarse");
    }
});
//Aplicar routerUsuarioSession
app.use("/ofertas/agregar", routerUsuarioSession);
app.use("/ofertas", routerUsuarioSession);
app.use("/oferta/eliminar/:id", routerUsuarioSession);
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
//app.use("/oferta/eliminar/:id", routerUsuario);

app.use(express.static('public'));
app.set('port', 8081);
app.set('db', 'mongodb://uo257947:uo257947@sdi-actividad2-606-608-shard-00-00-na9yb.mongodb.net:27017,sdi-actividad2-606-608-shard-00-01-na9yb.mongodb.net:27017,sdi-actividad2-606-608-shard-00-02-na9yb.mongodb.net:27017/test?ssl=true&replicaSet=sdi-actividad2-606-608-shard-0&authSource=admin&retryWrites=true');
app.set('clave', 'abcdefg');
app.set('crypto', crypto);
app.set('logger', logger);


require("./routes/rusuarios.js")(app, swig, gestorBD);
require("./routes/rofertas.js")(app, swig, gestorBD);
require("./routes/rapiofertas.js")(app, gestorBD);


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
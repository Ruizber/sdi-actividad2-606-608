var express = require('express');
var app = express();
var crypto = require('crypto');


var expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

var swig = require('swig-templates');
var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(express.static('public'));
app.set('port', 8081);
app.set('clave','abcdefg');
app.set('crypto',crypto);


require("./routes/rusuarios.js")(app, swig);
require("./routes/rofertas.js")(app, swig);

app.listen(app.get('port'), function() {
    console.log("Servidor activo");
});
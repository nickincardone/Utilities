var connect = require('connect');
var serveStatic = require('serve-static');
console.log('server started and listening on 8080 ...');
connect().use(serveStatic(__dirname)).listen(8080);
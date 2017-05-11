var connect = require("connect");
var moment = require("moment");
var port = 9010;
var logtext="", cnt=0;

var app = connect()
	.use('/', function (req, res, next) {
		next();
		var msg =  "[" + (cnt++) + "] - " + moment().format("DD MMM YYYY HH:mm:ss") + ' - ' + req.url;
		logtext += msg+'\n';
		console.log(msg);
	})
	.use('/log', function (req, res, next) {
		res.end(logtext);
	})
	.use('/bower_components', connect.static('./bower_components'))
	.use('/', connect.static('app'))
	.listen(port);

console.log("partiti sulla porta "+port);
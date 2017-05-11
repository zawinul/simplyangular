var connect = require("connect");

var app = connect()
	//.use('/simplyangular/bower_components', connect.static('./bower_components'))
	
	.use('/simplyangular', connect.static('../WebContent/'))
	.listen(9876);

console.log("partiti sulla porta 9876");
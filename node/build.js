const fs = require('fs-extra');
var sorgente = 'app'; 
var	destinazione =  '../WebContent'; 

//copia('./app', destinazione);
copia('./bower_components', destinazione+'/bower_components');

function copia(from, to) {
	fs.copy(from, to, {overwrite:true }, function(err){
		if (err) 
			return console.error(err)
		console.log('copiata '+from);
	})  
}

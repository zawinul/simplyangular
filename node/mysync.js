
const fs = require('fs-extra');
const util = require('util');
var coda = [];
var curr, fromstats=null, tostats=null;
var verbose = false;
var cntFileCheck = 0, cntFileCopy = 0, cntDirCreated = 0, cntError = 0;

function copiafile() {
	cntFileCopy++;
	function onData(err, data)  {
		if (err) {
			cntError++;
			console.log(err);
			scoda();
		}
		else
			fs.writeFile(curr.to, data, scoda);
	}
	fs.readFile(curr.from, onData);
}

function gestisciFile() {
	cntFileCheck++;
	fs.stat(curr.to, function(err, stats){
		if (err) {// il file probabilmente non esiste
			console.log(curr.to+' non esiste');
			copiafile();
		}
		else {
			tostats = stats;
			var fromtime = fromstats.mtime.getTime();
			var totime = tostats.mtime.getTime();
			if (totime>=fromtime) {
				if (verbose)
					console.log(curr.from+' non copiato');
				scoda();
			}
			else
				copiafile();
		}
	})
}

function gestisciDirectory() {
	fs.stat(curr.to, function(err, stats){
		if (err) {// la directory file probabilmente non esiste
			console.log(curr.to+' non esiste');
			cntDirCreated++;
			return fs.mkdir(curr.to, gestisciDirectory);
		}

		fs.readdir(curr.from, function(err, files){
			if (err) {
				cntError++;
				console.log("ERR 2: "+err);
				return;
			}
			for(var i=0;i<files.length;i++)
				coda.push({
					from: curr.from+'/'+files[i],
					to: curr.to+'/'+files[i]
				});
			scoda();
		});
	});
}

function scoda() {
	if (coda.length<=0) {
		console.log(`ho finito
			cntFileCheck = ${cntFileCheck}, 
			cntFileCopy = ${cntFileCopy}, 
			cntDirCreated = ${cntDirCreated}, 
			cntError = ${cntError}
		`);
		return;
	}
	curr = coda.splice(0,1)[0];
	if (verbose)
		console.log('scoda len='+coda.length+' from='+curr.from+' to='+curr.to);
	fs.stat(curr.from, function(err, stats){
		fromstats = stats;
		if (err) {
			cntError++;
			console.log("ERR3: "+err);
			return;
		}
		if (stats.isDirectory()) 
			gestisciDirectory();
		else if (stats.isFile())
			gestisciFile();
	});
}

coda.push({from:'./bower_components', to:'../WebContent/bower_components'});
scoda();

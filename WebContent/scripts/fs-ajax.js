(function(){

var ajaxurl = 'ajax/';
if (location.port==9876)
	ajaxurl = 'http://'+location.hostname+':8088'+location.pathname+'ajax/';

function send(name, params, callback) {
	var url = ajaxurl+name;
	var cfg = {
		method: "POST", 
		url: url,
		data: JSON.stringify(params || {}),
		dataType:'json',
		headers: {"Content-Type": "application/json"},
		success:onData
	};
	
	return $.ajax(cfg);
	
	function onData(data) {
		console.log({sendXHRresponse:data});
		if (callback)
			callback(data);
	}
}
	
	
window.fsAjax = {
	send:send
};


})();


'use strict';

var conf = {};
var rootScope;
var mainInjector;

//-----------------------------------------------------------------------------------

(function () {

	var app;
	var defaultState;

	function main() {
		var w1 = richiediConfigurazione();
		//var w2 = getInitialData();
		w1.done(function (data) {
			console.log({configurazione:data});
			initAngular();
		});

	}
	window.main = main;

	function configurazioneAngularCallback($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {
		console.log('configurazioneAngularCallback begin');
		$ocLazyLoadProvider.config({
			debug: false,
			events: true
		});


		var uid=0;
		function initState(cfg) {
			if (cfg.dipendenze) {
				var files=[];
				for(var i=0; i<cfg.dipendenze.length;i++)
					files.push(cfg.dipendenze[i]);
				cfg.resolve = {
					loadMe:	function ($ocLazyLoad) {
						return $ocLazyLoad.load({
							name: APPNAME,
							files: files
						});
					}
				};
			}
			if (cfg.isDefault)
				defaultState = cfg.name;
			var stato = $stateProvider.state(cfg.name, cfg);
		};

		for (var i = 0; i < stati_applicazione.length; i++) {
			initState(stati_applicazione[i]);
		}
		if (defaultState)
			$urlRouterProvider.otherwise(defaultState);
		console.log('configurazioneAngularCallback end');
	}


	function avvioAngularCallback($rootScope, $state, $injector) {
		// inizializza il rootScope e altre variabili globali, fa partire la prima pagina se necessario
		mainInjector = $injector;
		rootScope = $rootScope;
		rootScope.conf = conf;
		rootScope.regex = {
			importi: /^[1-9][0-9]*(\,[0-9]{2})?$/
		}
		rootScope.conf = conf;

		fsAjax.send('session/getConfiguration', {}, function (data) {
			rootScope.conf = conf = data.conf;
			rootScope.$apply();
		});

		console.log("AVVIO st=" + $state.current.name);

		console.log({ avvioStato: $state });
		var target = ($state.current.name)
			? $state.current.name
			: ((defaultState) ? defaultState : null);
		$state.go(target)
	}



	function initAngular() {
		var app = angular.module(APPNAME, [
			'ng',
			'ngLocale',
			'oc.lazyLoad',
			'ui.router',
			'ui.bootstrap',
			"ui.bootstrap.tpls",
			'ui.bootstrap.datepicker',
			'lr.upload'
		]);

		app.config(configurazioneAngularCallback);
		app.run(avvioAngularCallback);
		var m = $('#angular-main');
		angular.bootstrap(m, [APPNAME]);

	}


	function richiediConfigurazione() {
		var ret = $.Deferred();
		fsAjax.send("session/getConfiguration", null, function (data) {
			console.log({ configdata: data });
			$.extend(conf, data.conf);
			ret.resolve(conf);
		});
		return ret;
	}




	$(main);


})();

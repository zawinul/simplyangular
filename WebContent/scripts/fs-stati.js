var stati_applicazione = [
	{
		name: 'dashboard',
		url: '/dashboard',
		templateUrl: 'views/dashboard/dashboard.html',
		//template:'<div>ciao</div>',
		controller: function () { },
		dipendenze: [
			
			"bower_components/font-awesome/css/font-awesome.min.css",
			"bower_components/datatables.net/js/jquery.dataTables.js",
			"bower_components/bootstrap/dist/js/bootstrap.min.js",
			"bower_components/json3/lib/json3.min.js",
			
			'scripts/directives/header/header.js',
			'scripts/directives/sidebar/sidebar.js',
			'scripts/directives/footer/footer.js',
			
			'views/dashboard/dashboard.css'
			
		]
	},
	{
		isDefault: true,
		name: 'dashboard.home',
		url: '/home',
		templateUrl: 'views/dashboard/home.html',
		controller: function () {
		},
		dipendenze: [
		]
	},
	{
		name: 'dashboard.act1',
		url: '/act1',
		template: '<h1 style="text-align:center;padding:30px">attività uno</h1>',
	},
	{
		name: 'dashboard.act2',
		url: '/act2',
		template: '<h1 style="text-align:center;padding:30px">attività due</h1>',
	},
	{
		name: 'dashboard.act3',
		url: '/act3',
		template: '<h1 style="text-align:center;padding:30px">attività tre</h1>',
	},
	{
		name: 'dashboard.report',
		url: '/report',
		templateUrl: 'views/report/report.html',
		controller: "reportController",
		dipendenze: [
			'views/report/report.js'
		]
	},
	{
		name: 'dashboard.spotify',
		url: '/spotify',
		templateUrl: 'views/spotify/spotify.html',
		controller: "spotifyController",
		dipendenze: [
			'views/spotify/spotify.js',
			'views/spotify/spotify.css'
		]
	}

]


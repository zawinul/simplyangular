'use strict';

(function() {

function f() {
	
	var descr = {
	    templateUrl:'scripts/directives/header/header.html',
	    restrict: 'E',
	    replace: true,
	};

	return descr;
}

angular.module(APPNAME).directive('header',f);

	
})();




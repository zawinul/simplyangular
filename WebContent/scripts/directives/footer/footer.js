'use strict';


(function() {

var descr = {
    templateUrl:'scripts/directives/footer/footer.html',
    restrict: 'E',
    replace: true,
};
function f() {
	return descr;
}

angular.module(APPNAME).directive('footer',f);

	
})();




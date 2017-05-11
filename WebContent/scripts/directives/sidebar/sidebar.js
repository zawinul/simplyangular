
(function() {

    function controller() {
    }
    
	function factory() {
	    var obj = {
	        templateUrl: 'scripts/directives/sidebar/sidebar.html',
	        restrict: 'E',
	        controller: controller
	    };
	    return obj;	
	}	
	
	angular.module(APPNAME).directive('sidebar', factory);

})();
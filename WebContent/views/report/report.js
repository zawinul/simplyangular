(function(){


function controller($scope) {
    $scope.msg = "io sono il report";
	$scope.getData = function() {
		fsAjax.send('report/getData', {n:15}, onReportData);
	}
	$scope.data = null;

	

	function onReportData(response) {
		$scope.data = response.data;	
		$scope.$apply();
	}

}


angular.module(APPNAME).controller("reportController", controller);

})()

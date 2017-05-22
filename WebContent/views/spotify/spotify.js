(function(){


function controller($scope, $element, $http) {
	window.ss=$scope;
	
    $scope.msg = "bla bla";
	$scope.data = null;
	$scope.albums = [];
	$scope.doSearch = function() {
		var query = $('.query', $element).val();
		$http({
    		url: 'https://api.spotify.com/v1/search', 
    		method: "GET",
    		params: { q: query, type: 'album', market:'it'}
 		}).then(function(response) {
            $scope.albums=response.data.albums.items;
    	});
	}

	
	$scope.getTracks = function(album, callback) {
		//alert(album.name);
	    $.ajax({
	        url: 'https://api.spotify.com/v1/albums/' + album.id,
	        data: { market:'it'},
	        success: function (response) {
	        	album.tracks = response.tracks;
	        	console.log(response);
	            $scope.$apply();
	        }
	    });
	}	
	
	var playing = null;
    var audioObject = new Audio();
	
	$scope.play = function(track) {
		if (playing)
			playing.suona = false;
		console.log({ptrack:track});
		playing = track;
        audioObject.src=track.preview_url;
        audioObject.play();
        audioObject.addEventListener('ended', function() {
        	//alert('ended');
			playing.suona = false;
			$scope.$apply();
        });
		track.suona=true;
	}
}



angular.module(APPNAME).controller("spotifyController", controller);

})()

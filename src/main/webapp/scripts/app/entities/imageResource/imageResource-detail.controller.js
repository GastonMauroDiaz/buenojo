'use strict';

angular.module('buenOjoApp')
    .controller('ImageResourceDetailController', function ($scope, $rootScope, $stateParams, entity, ImageResource) {
        $scope.imageResource = entity;
        $scope.load = function (id) {
            ImageResource.get({id: id}, function(result) {
                $scope.imageResource = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:imageResourceUpdate', function(event, result) {
            $scope.imageResource = result;
        });
        $scope.$on('$destroy', unsubscribe);


        $scope.byteSize = function (imagePath) {
            if (!angular.isString(imagePath)) {
                return '';
            }
            
            function size(file) {
            	var fileReader = new FileReader();
            	fileReader.readAsURL(imagePath);
            	fileReader.onload = function (e) {
            		var data = e.target.result
            		$scope.apply(function(){
            			$scope.imageResource.fileSize = formatAsBytes(data.length);
            		});
            	}
                   
            }
            function formatAsBytes(size) {
                return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
            }

            return formatAsBytes(size(file));
        };
    });

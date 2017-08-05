'use strict';

angular.module('buenOjoApp').controller('SatelliteImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SatelliteImage','Upload' ,'ImageResource',
        function($scope, $stateParams, $modalInstance, entity, SatelliteImage,Upload ,ImageResource) {

        $scope.satelliteImage = entity;
        $scope.imageResources = ImageResource.query();
        $scope.load = function(id) {
            SatelliteImage.get({id : id}, function(result) {
                $scope.satelliteImage = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:satelliteImageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.satelliteImage.id != null) {
                SatelliteImage.update($scope.satelliteImage, onSaveFinished);
            } else {
                SatelliteImage.save($scope.satelliteImage, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
        };

        
        
      
}]);

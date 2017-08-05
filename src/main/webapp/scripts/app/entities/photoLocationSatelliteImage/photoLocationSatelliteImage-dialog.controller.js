'use strict';

angular.module('buenOjoApp').controller('PhotoLocationSatelliteImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'PhotoLocationSatelliteImage', 'PhotoLocationKeyword', 'SatelliteImage',
        function($scope, $stateParams, $modalInstance, $q, entity, PhotoLocationSatelliteImage, PhotoLocationKeyword, SatelliteImage) {

        $scope.photoLocationSatelliteImage = entity;
        $scope.photolocationkeywords = PhotoLocationKeyword.query();
        $scope.satelliteimages = SatelliteImage.query({filter: 'photolocationsatelliteimage-is-null'});
        $q.all([$scope.photoLocationSatelliteImage.$promise, $scope.satelliteimages.$promise]).then(function() {
            if (!$scope.photoLocationSatelliteImage.satelliteImage.id) {
                return $q.reject();
            }
            return SatelliteImage.get({id : $scope.photoLocationSatelliteImage.satelliteImage.id}).$promise;
        }).then(function(satelliteImage) {
            $scope.satelliteimages.push(satelliteImage);
        });
        $scope.load = function(id) {
            PhotoLocationSatelliteImage.get({id : id}, function(result) {
                $scope.photoLocationSatelliteImage = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationSatelliteImageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.photoLocationSatelliteImage.id != null) {
                PhotoLocationSatelliteImage.update($scope.photoLocationSatelliteImage, onSaveFinished);
            } else {
                PhotoLocationSatelliteImage.save($scope.photoLocationSatelliteImage, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

'use strict';

angular.module('buenOjoApp').controller('PhotoLocationExtraSatelliteImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'PhotoLocationExtraSatelliteImage', 'PhotoLocationSatelliteImage', 'Course',
        function($scope, $stateParams, $modalInstance, $q, entity, PhotoLocationExtraSatelliteImage, PhotoLocationSatelliteImage, Course) {

        $scope.photoLocationExtraSatelliteImage = entity;
        $scope.images = PhotoLocationSatelliteImage.query({filter: 'photolocationextrasatelliteimage-is-null'});
        $q.all([$scope.photoLocationExtraSatelliteImage.$promise, $scope.images.$promise]).then(function() {
            if (!$scope.photoLocationExtraSatelliteImage.image.id) {
                return $q.reject();
            }
            return PhotoLocationSatelliteImage.get({id : $scope.photoLocationExtraSatelliteImage.image.id}).$promise;
        }).then(function(image) {
            $scope.images.push(image);
        });
        $scope.courses = Course.query({filter: 'photolocationextrasatelliteimage-is-null'});
        $q.all([$scope.photoLocationExtraSatelliteImage.$promise, $scope.courses.$promise]).then(function() {
            if (!$scope.photoLocationExtraSatelliteImage.course.id) {
                return $q.reject();
            }
            return Course.get({id : $scope.photoLocationExtraSatelliteImage.course.id}).$promise;
        }).then(function(course) {
            $scope.courses.push(course);
        });
        $scope.load = function(id) {
            PhotoLocationExtraSatelliteImage.get({id : id}, function(result) {
                $scope.photoLocationExtraSatelliteImage = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationExtraSatelliteImageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.photoLocationExtraSatelliteImage.id != null) {
                PhotoLocationExtraSatelliteImage.update($scope.photoLocationExtraSatelliteImage, onSaveFinished);
            } else {
                PhotoLocationExtraSatelliteImage.save($scope.photoLocationExtraSatelliteImage, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

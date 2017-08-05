'use strict';

angular.module('buenOjoApp').controller('PhotoLocationExerciseDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'PhotoLocationExercise', 'PhotoLocationBeacon', 'PhotoLocationSightPair', 'PhotoLocationKeyword', 'PhotoLocationImage', 'PhotoLocationSatelliteImage', 'Course',
        function($scope, $stateParams, $modalInstance, $q, entity, PhotoLocationExercise, PhotoLocationBeacon, PhotoLocationSightPair, PhotoLocationKeyword, PhotoLocationImage, PhotoLocationSatelliteImage, Course) {

        $scope.courses = Course.query();
        $scope.photoLocationExercise = entity;
        // $scope.levels = Level.query();
        $scope.beacons = PhotoLocationBeacon.query({filter: 'photolocationexercise-is-null'});
        $q.all([$scope.photoLocationExercise.$promise, $scope.beacons.$promise]).then(function() {
            if (!$scope.photoLocationExercise.beacon) {
                return $q.reject();
            }
            return PhotoLocationBeacon.get({id : $scope.photoLocationExercise.beacon.id}).$promise;
        }).then(function(beacon) {
            $scope.beacons.push(beacon);
        });
        $scope.photolocationsightpairs = PhotoLocationSightPair.query();
        $scope.photolocationkeywords = PhotoLocationKeyword.query();
        $scope.terrainphotos = PhotoLocationImage.query({filter: 'exercise-is-null'});
        $q.all([$scope.photoLocationExercise.$promise, $scope.terrainphotos.$promise]).then(function() {
            if (!$scope.photoLocationExercise.terrainPhoto) {
                return $q.reject();
            }
            return PhotoLocationImage.get({id : $scope.photoLocationExercise.terrainPhoto.id}).$promise;
        }).then(function(terrainPhoto) {
            $scope.terrainphotos.push(terrainPhoto);
        });
        $scope.photolocationsatelliteimages = PhotoLocationSatelliteImage.query();
        $scope.load = function(id) {
            PhotoLocationExercise.get({id : id}, function(result) {
               $scope.photoLocationExercise = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationExerciseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.photoLocationExercise.id != null) {
                PhotoLocationExercise.update($scope.photoLocationExercise, onSaveFinished);
            } else {
                debugger;
                PhotoLocationExercise.save($scope.photoLocationExercise, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

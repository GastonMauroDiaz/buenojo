'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationExerciseDetailController', function ($scope, $rootScope, $stateParams, entity, PhotoLocationExercise, PhotoLocationBeacon, PhotoLocationSightPair, PhotoLocationKeyword, PhotoLocationImage, PhotoLocationSatelliteImage) {
        $scope.photoLocationExercise = entity;
        $scope.load = function (id) {
          debugger;
            PhotoLocationExercise.get({id: id}, function(result) {
                $scope.photoLocationExercise = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:photoLocationExerciseUpdate', function(event, result) {
            $scope.photoLocationExercise = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

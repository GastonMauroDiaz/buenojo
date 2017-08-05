'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseDelimitedAreaController', function ($scope, $state, $modal, HangManExerciseDelimitedArea) {
      
        $scope.hangManExerciseDelimitedAreas = [];
        $scope.loadAll = function() {
            HangManExerciseDelimitedArea.query(function(result) {
               $scope.hangManExerciseDelimitedAreas = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.hangManExerciseDelimitedArea = {
                x: null,
                y: null,
                radius: null,
                id: null
            };
        };
    });

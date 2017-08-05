'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseController', function ($scope, $state, $modal, HangManExercise) {
      
        $scope.hangManExercises = [];
        $scope.loadAll = function() {
            HangManExercise.query(function(result) {
               $scope.hangManExercises = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.hangManExercise = {
                task: null,
                exerciseOrder: null,
                id: null
            };
        };
    });

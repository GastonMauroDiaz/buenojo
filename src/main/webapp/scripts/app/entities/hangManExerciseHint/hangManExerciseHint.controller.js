'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseHintController', function ($scope, $state, $modal, HangManExerciseHint) {
      
        $scope.hangManExerciseHints = [];
        $scope.loadAll = function() {
            HangManExerciseHint.query(function(result) {
               $scope.hangManExerciseHints = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.hangManExerciseHint = {
                text: null,
                x: null,
                y: null,
                id: null
            };
        };
    });

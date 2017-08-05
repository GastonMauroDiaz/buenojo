'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseOptionController', function ($scope, $state, $modal, HangManExerciseOption) {
      
        $scope.hangManExerciseOptions = [];
        $scope.loadAll = function() {
            HangManExerciseOption.query(function(result) {
               $scope.hangManExerciseOptions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.hangManExerciseOption = {
                text: null,
                isCorrect: false,
                id: null
            };
        };
    });

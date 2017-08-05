'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseImageResourceController', function ($scope, $state, $modal, HangManExerciseImageResource) {
      
        $scope.hangManExerciseImageResources = [];
        $scope.loadAll = function() {
            HangManExerciseImageResource.query(function(result) {
               $scope.hangManExerciseImageResources = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.hangManExerciseImageResource = {
                id: null
            };
        };
    });

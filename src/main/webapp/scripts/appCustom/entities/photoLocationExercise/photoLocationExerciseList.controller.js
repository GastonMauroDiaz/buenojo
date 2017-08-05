'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationExerciseListController', function ($scope, $state, $modal, PhotoLocationExercise) {

        $scope.exercises = [];
        $scope.loadAll = function() {
            PhotoLocationExercise.query(function(result) {
               $scope.exercises = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.exercises = {}
        };
    });

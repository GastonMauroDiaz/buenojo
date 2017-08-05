'use strict';

angular.module('buenOjoApp')
    .controller('ImageCompletionExerciseListController', function ($scope, $state, $modal, ImageCompletionExercise) {

        $scope.imageCompletionExercises = [];
        $scope.loadAll = function() {
            ImageCompletionExercise.query(function(result) {
               $scope.imageCompletionExercises = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.imageCompletionExercises = {
                name: null,
                id: null
            };
        };
    });

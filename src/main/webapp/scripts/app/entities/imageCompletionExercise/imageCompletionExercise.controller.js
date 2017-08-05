'use strict';

angular.module('buenOjoApp')
    .controller('ImageCompletionExerciseController', function ($scope, ImageCompletionExercise) {
        $scope.imageCompletionExercises = [];
        $scope.loadAll = function() {
            ImageCompletionExercise.query(function(result) {
               $scope.imageCompletionExercises = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ImageCompletionExercise.get({id: id}, function(result) {
                $scope.imageCompletionExercise = result;
                $('#deleteImageCompletionExerciseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ImageCompletionExercise.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteImageCompletionExerciseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.imageCompletionExercise = {
                id: null
            };
        };
    });

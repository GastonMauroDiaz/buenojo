'use strict';

angular.module('buenOjoApp')
    .controller('LoadedExerciseController', function ($scope, LoadedExercise) {
        $scope.loadedExercises = [];
        $scope.loadAll = function() {
            LoadedExercise.query(function(result) {
               $scope.loadedExercises = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            LoadedExercise.get({id: id}, function(result) {
                $scope.loadedExercise = result;
                $('#deleteLoadedExerciseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            LoadedExercise.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLoadedExerciseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.loadedExercise = {
                exerciseId: null,
                id: null
            };
        };
    });

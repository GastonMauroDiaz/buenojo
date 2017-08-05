'use strict';

angular.module('buenOjoApp')
    .controller('ExerciseController', function ($scope, Exercise) {
        $scope.exercises = [];
        $scope.loadAll = function() {
            Exercise.query(function(result) {
               $scope.exercises = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Exercise.get({id: id}, function(result) {
                $scope.exercise = result;
                $('#deleteExerciseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Exercise.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExerciseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.exercise = {
                name: null,
                description: null,
                totalScore: null,
                id: null
            };
        };
    });

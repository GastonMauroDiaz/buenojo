'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationExerciseController', function ($scope, PhotoLocationExercise) {
        $scope.photoLocationExercises = [];
        $scope.loadAll = function() {
            PhotoLocationExercise.query(function(result) {
               $scope.photoLocationExercises = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PhotoLocationExercise.get({id: id}, function(result) {
                $scope.photoLocationExercise = result;
                $('#deletePhotoLocationExerciseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PhotoLocationExercise.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoLocationExerciseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photoLocationExercise = {
                totalScore: null,
                totalTimeInSeconds: null,
                id: null
            };
        };
    });

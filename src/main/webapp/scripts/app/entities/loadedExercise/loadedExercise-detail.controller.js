'use strict';

angular.module('buenOjoApp')
    .controller('LoadedExerciseDetailController', function ($scope, $rootScope, $stateParams, entity, LoadedExercise, LoaderTrace) {
        $scope.loadedExercise = entity;
        $scope.load = function (id) {
            LoadedExercise.get({id: id}, function(result) {
                $scope.loadedExercise = result;
            });
        };
        $rootScope.$on('buenOjoApp:loadedExerciseUpdate', function(event, result) {
            $scope.loadedExercise = result;
        });
    });

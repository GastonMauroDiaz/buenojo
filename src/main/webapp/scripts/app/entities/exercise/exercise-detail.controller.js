'use strict';

angular.module('buenOjoApp')
    .controller('ExerciseDetailController', function ($scope, $rootScope, $stateParams, entity, Exercise, Level, ExerciseTip) {
        $scope.exercise = entity;
        $scope.load = function (id) {
            Exercise.get({id: id}, function(result) {
                $scope.exercise = result;
            });
        };
        $rootScope.$on('buenOjoApp:exerciseUpdate', function(event, result) {
            $scope.exercise = result;
        });
    });

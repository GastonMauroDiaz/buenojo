'use strict';

angular.module('buenOjoApp')
    .controller('ExerciseTipDetailController', function ($scope, $rootScope, $stateParams, entity, ExerciseTip, Exercise) {
        $scope.exerciseTip = entity;
        $scope.load = function (id) {
            ExerciseTip.get({id: id}, function(result) {
                $scope.exerciseTip = result;
            });
        };
        $rootScope.$on('buenOjoApp:exerciseTipUpdate', function(event, result) {
            $scope.exerciseTip = result;
        });
    });

'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseDetailController', function ($scope, $rootScope, $stateParams, entity, HangManExercise, ImageResource) {
        $scope.hangManExercise = entity;
        $scope.load = function (id) {
            HangManExercise.get({id: id}, function(result) {
                $scope.hangManExercise = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:hangManExerciseUpdate', function(event, result) {
            $scope.hangManExercise = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

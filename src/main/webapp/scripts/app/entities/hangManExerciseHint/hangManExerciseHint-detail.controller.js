'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseHintDetailController', function ($scope, $rootScope, $stateParams, entity, HangManExerciseHint, HangManExercise) {
        $scope.hangManExerciseHint = entity;
        $scope.load = function (id) {
            HangManExerciseHint.get({id: id}, function(result) {
                $scope.hangManExerciseHint = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:hangManExerciseHintUpdate', function(event, result) {
            $scope.hangManExerciseHint = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

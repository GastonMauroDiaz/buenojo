'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseOptionDetailController', function ($scope, $rootScope, $stateParams, entity, HangManExerciseOption, HangManExercise) {
        $scope.hangManExerciseOption = entity;
        $scope.load = function (id) {
            HangManExerciseOption.get({id: id}, function(result) {
                $scope.hangManExerciseOption = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:hangManExerciseOptionUpdate', function(event, result) {
            $scope.hangManExerciseOption = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

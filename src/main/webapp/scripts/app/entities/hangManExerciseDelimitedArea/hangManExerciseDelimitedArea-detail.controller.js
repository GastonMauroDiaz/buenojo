'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseDelimitedAreaDetailController', function ($scope, $rootScope, $stateParams, entity, HangManExerciseDelimitedArea, HangManExercise) {
        $scope.hangManExerciseDelimitedArea = entity;
        $scope.load = function (id) {
            HangManExerciseDelimitedArea.get({id: id}, function(result) {
                $scope.hangManExerciseDelimitedArea = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:hangManExerciseDelimitedAreaUpdate', function(event, result) {
            $scope.hangManExerciseDelimitedArea = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

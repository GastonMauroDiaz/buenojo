'use strict';

angular.module('buenOjoApp')
    .controller('HangManExerciseImageResourceDetailController', function ($scope, $rootScope, $stateParams, entity, HangManExerciseImageResource, HangManExercise, ImageResource) {
        $scope.hangManExerciseImageResource = entity;
        $scope.load = function (id) {
            HangManExerciseImageResource.get({id: id}, function(result) {
                $scope.hangManExerciseImageResource = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:hangManExerciseImageResourceUpdate', function(event, result) {
            $scope.hangManExerciseImageResource = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

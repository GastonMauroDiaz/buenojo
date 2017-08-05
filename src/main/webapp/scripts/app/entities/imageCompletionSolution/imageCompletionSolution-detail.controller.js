'use strict';

angular.module('buenOjoApp')
    .controller('ImageCompletionSolutionDetailController', function ($scope, $rootScope, $stateParams, entity, ImageCompletionSolution, ImageCompletionExercise, TagPair) {
        $scope.imageCompletionSolution = entity;
        $scope.load = function (id) {
            ImageCompletionSolution.get({id: id}, function(result) {
                $scope.imageCompletionSolution = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:imageCompletionSolutionUpdate', function(event, result) {
            $scope.imageCompletionSolution = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

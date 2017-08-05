'use strict';

angular.module('buenOjoApp')
    .controller('ImageCompletionExerciseDetailController', function ($scope, $rootScope, $stateParams, entity, ImageCompletionExercise, ImageCompletionSolution, Tag, SatelliteImage,TagCloud) {
        $scope.imageCompletionExercise = entity;
        $scope.load = function (id) {
            ImageCompletionExercise.get({id: id}, function(result) {
                $scope.imageCompletionExercise = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:imageCompletionExerciseUpdate', function(event, result) {
            $scope.imageCompletionExercise = result;
        });
        $scope.$on('$destroy', unsubscribe);
        $scope.generateTagCloud = function (id) {
            TagCloud.get({id: $scope.imageCompletionExercise.id}, function(result) {
                $scope.imageCompletionExercise.tagCloud = result;
            });
        };

    });

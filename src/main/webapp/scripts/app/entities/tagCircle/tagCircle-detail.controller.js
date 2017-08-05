'use strict';

angular.module('buenOjoApp')
    .controller('TagCircleDetailController', function ($scope, $rootScope, $stateParams, entity, TagCircle, ImageCompletionExercise) {
        $scope.tagCircle = entity;
        $scope.load = function (id) {
            TagCircle.get({id: id}, function(result) {
                $scope.tagCircle = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:tagCircleUpdate', function(event, result) {
            $scope.tagCircle = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

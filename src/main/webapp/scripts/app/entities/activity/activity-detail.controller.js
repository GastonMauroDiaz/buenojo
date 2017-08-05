'use strict';

angular.module('buenOjoApp')
    .controller('ActivityDetailController', function ($scope, $rootScope, $stateParams, entity, Activity, Level, ActivityType) {
        $scope.activity = entity;
        $scope.load = function (id) {
            Activity.get({id: id}, function(result) {
                $scope.activity = result;
            });
        };
        $scope.goToExercise = function(activity) {
            ActivityType.goToExerciseDetail(activity);
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:activityUpdate', function(event, result) {
            $scope.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

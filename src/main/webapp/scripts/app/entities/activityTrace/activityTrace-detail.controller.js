'use strict';

angular.module('buenOjoApp')
    .controller('ActivityTraceDetailController', function ($scope, $rootScope, $stateParams, entity, ActivityTrace, Enrollment, Activity) {
        $scope.activityTrace = entity;
        $scope.load = function (id) {
            ActivityTrace.get({id: id}, function(result) {
                $scope.activityTrace = result;
            });
        };
        $rootScope.$on('buenOjoApp:activityTraceUpdate', function(event, result) {
            $scope.activityTrace = result;
        });
    });

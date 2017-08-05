'use strict';

angular.module('buenOjoApp')
    .controller('CourseLevelSessionDetailController', function ($scope, $rootScope, $stateParams, entity, CourseLevelSession, CourseLevelMap, User) {
        $scope.courseLevelSession = entity;
        $scope.load = function (id) {
            CourseLevelSession.get({id: id}, function(result) {
                $scope.courseLevelSession = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:courseLevelSessionUpdate', function(event, result) {
            $scope.courseLevelSession = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

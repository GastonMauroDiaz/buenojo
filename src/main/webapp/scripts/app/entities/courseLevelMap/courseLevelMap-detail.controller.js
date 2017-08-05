'use strict';

angular.module('buenOjoApp')
    .controller('CourseLevelMapDetailController', function ($scope, $rootScope, $stateParams, entity, CourseLevelMap, Course, Level) {
        $scope.courseLevelMap = entity;
        $scope.load = function (id) {
            CourseLevelMap.get({id: id}, function(result) {
                $scope.courseLevelMap = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:courseLevelMapUpdate', function(event, result) {
            $scope.courseLevelMap = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

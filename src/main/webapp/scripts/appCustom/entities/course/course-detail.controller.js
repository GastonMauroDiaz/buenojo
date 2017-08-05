'use strict';

angular.module('buenOjoApp')
    .controller('CourseDetailController', function ($scope, $rootScope, $stateParams, entity, Course, Level) {
        $scope.course = entity;
        $scope.load = function (id) {
            Course.get({id: id}, function(result) {
                $scope.course = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:courseUpdate', function(event, result) {
            $scope.course = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

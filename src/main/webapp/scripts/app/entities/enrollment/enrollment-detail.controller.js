'use strict';

angular.module('buenOjoApp')
    .controller('EnrollmentDetailController', function ($scope, $rootScope, $stateParams, entity, Enrollment, Course, User) {
        $scope.enrollment = entity;
        $scope.load = function (id) {
            Enrollment.get({id: id}, function(result) {
                $scope.enrollment = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:enrollmentUpdate', function(event, result) {
            $scope.enrollment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

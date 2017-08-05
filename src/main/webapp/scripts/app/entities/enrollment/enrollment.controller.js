'use strict';

angular.module('buenOjoApp')
    .controller('EnrollmentController', function ($scope, $state, $modal, Enrollment) {
      
        $scope.enrollments = [];
        $scope.loadAll = function() {
            Enrollment.query(function(result) {
               $scope.enrollments = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.enrollment = {
                status: null,
                id: null
            };
        };
    });

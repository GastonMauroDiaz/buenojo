'use strict';

angular.module('buenOjoApp')
	.controller('EnrollmentDeleteController', function($scope, $modalInstance, entity, Enrollment) {

        $scope.enrollment = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Enrollment.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
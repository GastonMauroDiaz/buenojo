'use strict';

angular.module('buenOjoApp')
	.controller('CourseLevelSessionDeleteController', function($scope, $modalInstance, entity, CourseLevelSession) {

        $scope.courseLevelSession = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CourseLevelSession.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
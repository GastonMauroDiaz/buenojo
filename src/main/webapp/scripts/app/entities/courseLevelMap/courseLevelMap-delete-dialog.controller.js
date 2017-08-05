'use strict';

angular.module('buenOjoApp')
	.controller('CourseLevelMapDeleteController', function($scope, $modalInstance, entity, CourseLevelMap) {

        $scope.courseLevelMap = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CourseLevelMap.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
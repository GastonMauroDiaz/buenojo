'use strict';

angular.module('buenOjoApp')
	.controller('LevelDeleteController', function($scope, $modalInstance, entity, Level) {

        $scope.level = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Level.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
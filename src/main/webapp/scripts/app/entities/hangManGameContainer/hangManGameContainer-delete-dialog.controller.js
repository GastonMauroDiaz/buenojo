'use strict';

angular.module('buenOjoApp')
	.controller('HangManGameContainerDeleteController', function($scope, $modalInstance, entity, HangManGameContainer) {

        $scope.hangManGameContainer = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HangManGameContainer.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
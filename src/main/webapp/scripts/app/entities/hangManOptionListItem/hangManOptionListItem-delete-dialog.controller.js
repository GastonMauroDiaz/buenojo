'use strict';

angular.module('buenOjoApp')
	.controller('HangManOptionListItemDeleteController', function($scope, $modalInstance, entity, HangManOptionListItem) {

        $scope.hangManOptionListItem = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HangManOptionListItem.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
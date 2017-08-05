'use strict';

angular.module('buenOjoApp')
	.controller('CurrentSessionCustomDeleteController', function($scope, $modalInstance, entity, CurrentSession) {

        $scope.currentSession = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CurrentSession.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
'use strict';

angular.module('buenOjoApp')
	.controller('HangManExerciseImageResourceDeleteController', function($scope, $modalInstance, entity, HangManExerciseImageResource) {

        $scope.hangManExerciseImageResource = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HangManExerciseImageResource.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
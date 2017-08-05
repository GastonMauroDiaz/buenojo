'use strict';

angular.module('buenOjoApp')
	.controller('HangManExerciseDelimitedAreaDeleteController', function($scope, $modalInstance, entity, HangManExerciseDelimitedArea) {

        $scope.hangManExerciseDelimitedArea = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HangManExerciseDelimitedArea.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
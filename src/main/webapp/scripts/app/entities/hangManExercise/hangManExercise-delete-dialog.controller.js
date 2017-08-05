'use strict';

angular.module('buenOjoApp')
	.controller('HangManExerciseDeleteController', function($scope, $modalInstance, entity, HangManExercise) {

        $scope.hangManExercise = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HangManExercise.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
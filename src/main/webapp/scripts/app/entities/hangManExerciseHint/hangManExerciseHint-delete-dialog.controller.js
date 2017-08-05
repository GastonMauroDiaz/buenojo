'use strict';

angular.module('buenOjoApp')
	.controller('HangManExerciseHintDeleteController', function($scope, $modalInstance, entity, HangManExerciseHint) {

        $scope.hangManExerciseHint = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HangManExerciseHint.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
'use strict';

angular.module('buenOjoApp')
	.controller('HangManExerciseOptionDeleteController', function($scope, $modalInstance, entity, HangManExerciseOption) {

        $scope.hangManExerciseOption = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            HangManExerciseOption.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
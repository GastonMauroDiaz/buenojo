'use strict';

angular.module('buenOjoApp')
	.controller('MultipleChoiceExerciseContainerDeleteController', function($scope, $modalInstance, entity, MultipleChoiceExerciseContainer) {

        $scope.multipleChoiceExerciseContainer = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MultipleChoiceExerciseContainer.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
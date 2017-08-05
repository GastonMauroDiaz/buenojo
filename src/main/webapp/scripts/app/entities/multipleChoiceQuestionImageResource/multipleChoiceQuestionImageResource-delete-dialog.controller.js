'use strict';

angular.module('buenOjoApp')
	.controller('MultipleChoiceQuestionImageResourceDeleteController', function($scope, $modalInstance, entity, MultipleChoiceQuestionImageResource) {

        $scope.multipleChoiceQuestionImageResource = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MultipleChoiceQuestionImageResource.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
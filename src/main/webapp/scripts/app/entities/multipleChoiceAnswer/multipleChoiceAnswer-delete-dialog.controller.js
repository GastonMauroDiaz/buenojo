'use strict';

angular.module('buenOjoApp')
	.controller('MultipleChoiceAnswerDeleteController', function($scope, $modalInstance, entity, MultipleChoiceAnswer) {

        $scope.multipleChoiceAnswer = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MultipleChoiceAnswer.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
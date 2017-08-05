'use strict';

angular.module('buenOjoApp')
	.controller('MultipleChoiceQuestionDeleteController', function($scope, $modalInstance, entity, MultipleChoiceQuestion) {

        $scope.multipleChoiceQuestion = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MultipleChoiceQuestion.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
'use strict';

angular.module('buenOjoApp').controller('MultipleChoiceAnswerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MultipleChoiceAnswer', 'MultipleChoiceQuestion',
        function($scope, $stateParams, $modalInstance, entity, MultipleChoiceAnswer, MultipleChoiceQuestion) {

        $scope.multipleChoiceAnswer = entity;
        $scope.multiplechoicequestions = MultipleChoiceQuestion.query();
        $scope.load = function(id) {
            MultipleChoiceAnswer.get({id : id}, function(result) {
                $scope.multipleChoiceAnswer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:multipleChoiceAnswerUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.multipleChoiceAnswer.id != null) {
                MultipleChoiceAnswer.update($scope.multipleChoiceAnswer, onSaveSuccess, onSaveError);
            } else {
                MultipleChoiceAnswer.save($scope.multipleChoiceAnswer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

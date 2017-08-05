'use strict';

angular.module('buenOjoApp').controller('MultipleChoiceExerciseContainerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MultipleChoiceExerciseContainer', 'MultipleChoiceQuestion',
        function($scope, $stateParams, $modalInstance, entity, MultipleChoiceExerciseContainer, MultipleChoiceQuestion) {

        $scope.multipleChoiceExerciseContainer = entity;
        $scope.multiplechoicequestions = MultipleChoiceQuestion.query();
        $scope.load = function(id) {
            MultipleChoiceExerciseContainer.get({id : id}, function(result) {
                $scope.multipleChoiceExerciseContainer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:multipleChoiceExerciseContainerUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.multipleChoiceExerciseContainer.id != null) {
                MultipleChoiceExerciseContainer.update($scope.multipleChoiceExerciseContainer, onSaveSuccess, onSaveError);
            } else {
                MultipleChoiceExerciseContainer.save($scope.multipleChoiceExerciseContainer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

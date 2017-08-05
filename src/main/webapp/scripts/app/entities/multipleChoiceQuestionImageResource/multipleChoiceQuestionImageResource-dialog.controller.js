'use strict';

angular.module('buenOjoApp').controller('MultipleChoiceQuestionImageResourceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MultipleChoiceQuestionImageResource', 'MultipleChoiceQuestion', 'ImageResource',
        function($scope, $stateParams, $modalInstance, entity, MultipleChoiceQuestionImageResource, MultipleChoiceQuestion, ImageResource) {

        $scope.multipleChoiceQuestionImageResource = entity;
        $scope.multiplechoicequestions = MultipleChoiceQuestion.query();
        $scope.imageresources = ImageResource.query();
        $scope.load = function(id) {
            MultipleChoiceQuestionImageResource.get({id : id}, function(result) {
                $scope.multipleChoiceQuestionImageResource = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:multipleChoiceQuestionImageResourceUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.multipleChoiceQuestionImageResource.id != null) {
                MultipleChoiceQuestionImageResource.update($scope.multipleChoiceQuestionImageResource, onSaveSuccess, onSaveError);
            } else {
                MultipleChoiceQuestionImageResource.save($scope.multipleChoiceQuestionImageResource, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

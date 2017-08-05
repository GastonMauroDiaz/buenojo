'use strict';

angular.module('buenOjoApp').controller('MultipleChoiceAnswerDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'MultipleChoiceAnswer', 'MultipleChoiceQuestion', 'ImageResource',
        function($scope, $stateParams, $modalInstance, $q, entity, MultipleChoiceAnswer, MultipleChoiceQuestion, ImageResource) {

        $scope.multipleChoiceAnswer = entity;
        $scope.multiplechoicequestions = MultipleChoiceQuestion.query();
        $scope.imageresources = ImageResource.query({filter: 'multiplechoiceanswer-is-null'});
        $q.all([$scope.multipleChoiceAnswer.$promise, $scope.imageresources.$promise]).then(function() {
            if (!$scope.multipleChoiceAnswer.imageResource.id) {
                return $q.reject();
            }
            return ImageResource.get({id : $scope.multipleChoiceAnswer.imageResource.id}).$promise;
        }).then(function(imageResource) {
            $scope.imageresources.push(imageResource);
        });
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

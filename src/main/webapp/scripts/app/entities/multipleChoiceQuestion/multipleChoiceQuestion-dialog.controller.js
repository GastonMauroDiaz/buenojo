'use strict';

angular.module('buenOjoApp').controller('MultipleChoiceQuestionDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'MultipleChoiceQuestion', 'MultipleChoiceExerciseContainer', 'ImageResource', 'MultipleChoiceSubjectSpecific',
        function($scope, $stateParams, $modalInstance, $q, entity, MultipleChoiceQuestion, MultipleChoiceExerciseContainer, ImageResource, MultipleChoiceSubjectSpecific) {

        $scope.multipleChoiceQuestion = entity;
        $scope.multiplechoiceexercisecontainers = MultipleChoiceExerciseContainer.query();
        $scope.imageresources = ImageResource.query({filter: 'multiplechoicequestion-is-null'});
        $q.all([$scope.multipleChoiceQuestion.$promise, $scope.imageresources.$promise]).then(function() {
            if (!$scope.multipleChoiceQuestion.imageResource.id) {
                return $q.reject();
            }
            return ImageResource.get({id : $scope.multipleChoiceQuestion.imageResource.id}).$promise;
        }).then(function(imageResource) {
            $scope.imageresources.push(imageResource);
        });
        $scope.multiplechoicesubjectspecifics = MultipleChoiceSubjectSpecific.query({filter: 'multiplechoicequestion-is-null'});
        $q.all([$scope.multipleChoiceQuestion.$promise, $scope.multiplechoicesubjectspecifics.$promise]).then(function() {
            if (!$scope.multipleChoiceQuestion.multipleChoiceSubjectSpecific.id) {
                return $q.reject();
            }
            return MultipleChoiceSubjectSpecific.get({id : $scope.multipleChoiceQuestion.multipleChoiceSubjectSpecific.id}).$promise;
        }).then(function(multipleChoiceSubjectSpecific) {
            $scope.multiplechoicesubjectspecifics.push(multipleChoiceSubjectSpecific);
        });
        $scope.load = function(id) {
            MultipleChoiceQuestion.get({id : id}, function(result) {
                $scope.multipleChoiceQuestion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:multipleChoiceQuestionUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.multipleChoiceQuestion.id != null) {
                MultipleChoiceQuestion.update($scope.multipleChoiceQuestion, onSaveSuccess, onSaveError);
            } else {
                MultipleChoiceQuestion.save($scope.multipleChoiceQuestion, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

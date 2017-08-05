'use strict';

angular.module('buenOjoApp').controller('MultipleChoiceSubjectSpecificDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MultipleChoiceSubjectSpecific', 'MultipleChoiceSubject',
        function($scope, $stateParams, $modalInstance, entity, MultipleChoiceSubjectSpecific, MultipleChoiceSubject) {

        $scope.multipleChoiceSubjectSpecific = entity;
        $scope.multiplechoicesubjects = MultipleChoiceSubject.query();
        $scope.load = function(id) {
            MultipleChoiceSubjectSpecific.get({id : id}, function(result) {
                $scope.multipleChoiceSubjectSpecific = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:multipleChoiceSubjectSpecificUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.multipleChoiceSubjectSpecific.id != null) {
                MultipleChoiceSubjectSpecific.update($scope.multipleChoiceSubjectSpecific, onSaveSuccess, onSaveError);
            } else {
                MultipleChoiceSubjectSpecific.save($scope.multipleChoiceSubjectSpecific, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

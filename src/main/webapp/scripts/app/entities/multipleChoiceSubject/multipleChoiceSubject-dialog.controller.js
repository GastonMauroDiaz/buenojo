'use strict';

angular.module('buenOjoApp').controller('MultipleChoiceSubjectDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MultipleChoiceSubject',
        function($scope, $stateParams, $modalInstance, entity, MultipleChoiceSubject) {

        $scope.multipleChoiceSubject = entity;
        $scope.load = function(id) {
            MultipleChoiceSubject.get({id : id}, function(result) {
                $scope.multipleChoiceSubject = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:multipleChoiceSubjectUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.multipleChoiceSubject.id != null) {
                MultipleChoiceSubject.update($scope.multipleChoiceSubject, onSaveSuccess, onSaveError);
            } else {
                MultipleChoiceSubject.save($scope.multipleChoiceSubject, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

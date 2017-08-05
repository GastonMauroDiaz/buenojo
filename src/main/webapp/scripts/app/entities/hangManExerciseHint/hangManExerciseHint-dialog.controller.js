'use strict';

angular.module('buenOjoApp').controller('HangManExerciseHintDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HangManExerciseHint', 'HangManExercise',
        function($scope, $stateParams, $modalInstance, entity, HangManExerciseHint, HangManExercise) {

        $scope.hangManExerciseHint = entity;
        $scope.hangmanexercises = HangManExercise.query();
        $scope.load = function(id) {
            HangManExerciseHint.get({id : id}, function(result) {
                $scope.hangManExerciseHint = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:hangManExerciseHintUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hangManExerciseHint.id != null) {
                HangManExerciseHint.update($scope.hangManExerciseHint, onSaveSuccess, onSaveError);
            } else {
                HangManExerciseHint.save($scope.hangManExerciseHint, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

'use strict';

angular.module('buenOjoApp').controller('HangManExerciseOptionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HangManExerciseOption', 'HangManExercise',
        function($scope, $stateParams, $modalInstance, entity, HangManExerciseOption, HangManExercise) {

        $scope.hangManExerciseOption = entity;
        $scope.hangmanexercises = HangManExercise.query();
        $scope.load = function(id) {
            HangManExerciseOption.get({id : id}, function(result) {
                $scope.hangManExerciseOption = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:hangManExerciseOptionUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hangManExerciseOption.id != null) {
                HangManExerciseOption.update($scope.hangManExerciseOption, onSaveSuccess, onSaveError);
            } else {
                HangManExerciseOption.save($scope.hangManExerciseOption, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

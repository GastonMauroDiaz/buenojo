'use strict';

angular.module('buenOjoApp').controller('HangManExerciseImageResourceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HangManExerciseImageResource', 'HangManExercise', 'ImageResource',
        function($scope, $stateParams, $modalInstance, entity, HangManExerciseImageResource, HangManExercise, ImageResource) {

        $scope.hangManExerciseImageResource = entity;
        $scope.hangmanexercises = HangManExercise.query();
        $scope.imageresources = ImageResource.query();
        $scope.load = function(id) {
            HangManExerciseImageResource.get({id : id}, function(result) {
                $scope.hangManExerciseImageResource = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:hangManExerciseImageResourceUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hangManExerciseImageResource.id != null) {
                HangManExerciseImageResource.update($scope.hangManExerciseImageResource, onSaveSuccess, onSaveError);
            } else {
                HangManExerciseImageResource.save($scope.hangManExerciseImageResource, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

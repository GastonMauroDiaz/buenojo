'use strict';

angular.module('buenOjoApp').controller('HangManExerciseDelimitedAreaDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'HangManExerciseDelimitedArea', 'HangManExercise',
        function($scope, $stateParams, $modalInstance, $q, entity, HangManExerciseDelimitedArea, HangManExercise) {

        $scope.hangManExerciseDelimitedArea = entity;
        $scope.hangmanexercises = HangManExercise.query({filter: 'hangmanexercisedelimitedarea-is-null'});
        $q.all([$scope.hangManExerciseDelimitedArea.$promise, $scope.hangmanexercises.$promise]).then(function() {
            if (!$scope.hangManExerciseDelimitedArea.hangManExercise.id) {
                return $q.reject();
            }
            return HangManExercise.get({id : $scope.hangManExerciseDelimitedArea.hangManExercise.id}).$promise;
        }).then(function(hangManExercise) {
            $scope.hangmanexercises.push(hangManExercise);
        });
        $scope.load = function(id) {
            HangManExerciseDelimitedArea.get({id : id}, function(result) {
                $scope.hangManExerciseDelimitedArea = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:hangManExerciseDelimitedAreaUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hangManExerciseDelimitedArea.id != null) {
                HangManExerciseDelimitedArea.update($scope.hangManExerciseDelimitedArea, onSaveSuccess, onSaveError);
            } else {
                HangManExerciseDelimitedArea.save($scope.hangManExerciseDelimitedArea, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

'use strict';

angular.module('buenOjoApp').controller('HangManExerciseDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'HangManExercise', 'ImageResource',
        function($scope, $stateParams, $modalInstance, $q, entity, HangManExercise, ImageResource) {

        $scope.hangManExercise = entity;
        $scope.images = ImageResource.query({filter: 'hangmanexercise-is-null'});
        $q.all([$scope.hangManExercise.$promise, $scope.images.$promise]).then(function() {
            if (!$scope.hangManExercise.image.id) {
                return $q.reject();
            }
            return ImageResource.get({id : $scope.hangManExercise.image.id}).$promise;
        }).then(function(image) {
            $scope.images.push(image);
        });
        $scope.highlightedareas = ImageResource.query({filter: 'hangmanexercise-is-null'});
        $q.all([$scope.hangManExercise.$promise, $scope.highlightedareas.$promise]).then(function() {
            if (!$scope.hangManExercise.highlightedArea.id) {
                return $q.reject();
            }
            return ImageResource.get({id : $scope.hangManExercise.highlightedArea.id}).$promise;
        }).then(function(highlightedArea) {
            $scope.highlightedareas.push(highlightedArea);
        });
        $scope.load = function(id) {
            HangManExercise.get({id : id}, function(result) {
                $scope.hangManExercise = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:hangManExerciseUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hangManExercise.id != null) {
                HangManExercise.update($scope.hangManExercise, onSaveSuccess, onSaveError);
            } else {
                HangManExercise.save($scope.hangManExercise, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

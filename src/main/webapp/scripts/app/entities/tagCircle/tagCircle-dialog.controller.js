'use strict';

angular.module('buenOjoApp').controller('TagCircleDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TagCircle', 'ImageCompletionExercise',
        function($scope, $stateParams, $modalInstance, entity, TagCircle, ImageCompletionExercise) {

        $scope.tagCircle = entity;
        $scope.imagecompletionexercises = ImageCompletionExercise.query();
        $scope.load = function(id) {
            TagCircle.get({id : id}, function(result) {
                $scope.tagCircle = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:tagCircleUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.tagCircle.id != null) {
                TagCircle.update($scope.tagCircle, onSaveFinished);
            } else {
                TagCircle.save($scope.tagCircle, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

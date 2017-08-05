'use strict';

angular.module('buenOjoApp').controller('ImageCompletionSolutionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ImageCompletionSolution', 'ImageCompletionExercise', 'TagPair',
        function($scope, $stateParams, $modalInstance, entity, ImageCompletionSolution, ImageCompletionExercise, TagPair) {

        $scope.imageCompletionSolution = entity;
        $scope.imagecompletionexercises = ImageCompletionExercise.query();
        $scope.tagpairs = TagPair.query();
        $scope.load = function(id) {
            ImageCompletionSolution.get({id : id}, function(result) {
                $scope.imageCompletionSolution = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:imageCompletionSolutionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.imageCompletionSolution.id != null) {
                ImageCompletionSolution.update($scope.imageCompletionSolution, onSaveFinished);
            } else {
                ImageCompletionSolution.save($scope.imageCompletionSolution, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

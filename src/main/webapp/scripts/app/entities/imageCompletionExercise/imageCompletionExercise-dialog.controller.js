'use strict';

angular.module('buenOjoApp').controller('ImageCompletionExerciseDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'ImageCompletionExercise', 'ImageCompletionSolution', 'Tag', 'SatelliteImage', 'TagCircle','Level',
        function($scope, $stateParams, $modalInstance, $q, entity, ImageCompletionExercise, ImageCompletionSolution, Tag, SatelliteImage, TagCircle, Level) {

        $scope.imageCompletionExercise = entity;
        $scope.imagecompletionsolutions = ImageCompletionSolution.query({filter: 'imagecompletionexercise-is-null'});
        $q.all([$scope.imageCompletionExercise.$promise, $scope.imagecompletionsolutions.$promise]).then(function() {
            if (!$scope.imageCompletionExercise.imageCompletionSolution.id) {
                return $q.reject();
            }
            return ImageCompletionSolution.get({id : $scope.imageCompletionExercise.imageCompletionSolution.id}).$promise;
        }).then(function(imageCompletionSolution) {
            $scope.imagecompletionsolutions.push(imageCompletionSolution);
        });
        $scope.tags = Tag.query();
        $scope.satelliteimages = SatelliteImage.query();
        $scope.tagcircles = TagCircle.query();
        $scope.levels= Level.query();
        $scope.load = function(id) {
            ImageCompletionExercise.get({id : id}, function(result) {
            	
                $scope.imageCompletionExercise = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:imageCompletionExerciseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.imageCompletionExercise.id != null) {
                ImageCompletionExercise.update($scope.imageCompletionExercise, onSaveFinished);
            } else {
                ImageCompletionExercise.save($scope.imageCompletionExercise, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

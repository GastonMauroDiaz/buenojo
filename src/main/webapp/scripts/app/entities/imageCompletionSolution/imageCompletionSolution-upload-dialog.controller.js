'use strict';

angular.module('buenOjoApp').controller('ImageCompletionSolutionUploadDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'Upload','entity', 'ImageCompletionSolution', 'ImageCompletionExercise', 'TagPair',
        function($scope, $stateParams, $modalInstance,Upload ,entity, ImageCompletionSolution, ImageCompletionExercise, TagPair) {

        $scope.imageCompletionSolution = entity;
        $scope.imageCompletionExercises = ImageCompletionExercise.query();
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
        
        
        $scope.upload = function (file,imageCompletionExercise) {
            Upload.upload({
                url: ('api/imageCompletionSolutions/upload/'+imageCompletionExercise.id),
                file: file
            }).then(function (resp) {
                
                $modalInstance.close(resp);
                onSaveFinished(resp);
            }, function (resp) {
                
                $modalInstance.close(resp);
                onSaveFinished(resp);
            }, function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                
                
            });
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

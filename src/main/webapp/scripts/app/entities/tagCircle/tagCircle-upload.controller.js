'use strict';

angular.module('buenOjoApp').controller('TagCircleUploadDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'Upload','entity', 'ImageCompletionExercise', 'TagCircle',
        function($scope, $stateParams, $modalInstance,Upload ,entity, ImageCompletionExercise, TagCircle) {


        $scope.imageCompletionExercises = ImageCompletionExercise.query();

        $scope.load = function(id) {

        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:imageCompletionExerciseUpdate', result);
            $modalInstance.close(result);
        };


        $scope.upload = function (file,imageCompletionExercise) {

            Upload.upload({
                url: ('api/tagCircle/upload/'+imageCompletionExercise.id),
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
        // $scope.save = function () {
        //     if ($scope.imageCompletionSolution.id != null) {
        //         ImageCompletionSolution.update($scope.imageCompletionSolution, onSaveFinished);
        //     } else {
        //         ImageCompletionSolution.save($scope.imageCompletionSolution, onSaveFinished);
        //     }
        // };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

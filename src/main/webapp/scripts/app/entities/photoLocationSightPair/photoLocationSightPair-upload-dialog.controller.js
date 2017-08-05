'use strict';

angular.module('buenOjoApp').controller('PhotoLocationSightPairUploadDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PhotoLocationSightPair', 'PhotoLocationExercise','Upload',
        function($scope, $stateParams, $modalInstance, entity, PhotoLocationSightPair, PhotoLocationExercise,Upload) {

        $scope.photoLocationSightPair = entity;
        $scope.exercise ={};
        $scope.photolocationexercises = PhotoLocationExercise.query();

        $scope.upload = function (file,exercise) {
          debugger;
            Upload.upload({
                url: ('api/photoLocationSightPairs/upload/'+exercise.id),
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
        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationSightPairUpdate', result);
            $modalInstance.close(result);
        };


        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

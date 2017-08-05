'use strict';

angular.module('buenOjoApp').controller('PhotoLocationExtraImageUploadDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'PhotoLocationExtraImage', 'PhotoLocationImage','Upload', 'Course',
        function($scope, $stateParams, $modalInstance, $q, entity, PhotoLocationExtraImage, PhotoLocationImage, Upload,Course) {

        $scope.photoLocationExtraImage = entity;
        $scope.courses = Course.query();


        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationExtraImageUpdate', result);
        };


        $scope.upload = function (file,course) {
            Upload.upload({
                url: ('api/photoLocationExtraImages/upload/'+course.id),
                file: file
            }).then(function (resp) {

                onSaveFinished(resp);
            }, function (resp) {

                onSaveFinished(resp);
            }, function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);


            });
        };
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

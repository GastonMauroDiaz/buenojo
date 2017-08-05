'use strict';

angular.module('buenOjoApp').controller('ExerciseTipUploadDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'Upload','entity', 'ExerciseTip', 'Course',
        function($scope, $stateParams, $modalInstance, Upload,entity, ExerciseTip, Course) {

        $scope.exerciseTip = entity;
        $scope.courses = Course.query();
       

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:exerciseTipUpdate', result);
//            $modalInstance.close(result);
        };

        
        $scope.upload = function (file,course) {
            Upload.upload({
                url: ('api/exerciseTips/upload/'+course.id),
                file: file
            }).then(function (resp) {
                
//                $modalInstance.close(resp);
                onSaveFinished(resp);
            }, function (resp) {
                
//                $modalInstance.close(resp);
                onSaveFinished(resp);
            }, function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                
                
            });
        };
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

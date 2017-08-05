'use strict';

angular.module('buenOjoApp').controller('ExerciseTipDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ExerciseTip', 'Exercise', 'Course',
        function($scope, $stateParams, $modalInstance, entity, ExerciseTip, Exercise, Course) {

    	$scope.courses = Course.query();
        $scope.exerciseTip = entity;
        $scope.exercises = Exercise.query();
        $scope.load = function(id) {
            ExerciseTip.get({id : id}, function(result) {
                $scope.exerciseTip = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:exerciseTipUpdate', result);
            $modalInstance.close(result);
        };

        $scope.upload = function (file,course) {
            Upload.upload({
                url: ('api/exerciseTips/upload/'+course.id),
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

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

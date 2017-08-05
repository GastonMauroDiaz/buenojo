'use strict';

angular.module('buenOjoApp').controller('EnrollmentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Enrollment', 'Course', 'User','Level',
        function($scope, $stateParams, $modalInstance, entity, Enrollment, Course, User,Level) {

        $scope.levels = [];
        $scope.enrollment = entity;
        $scope.courses = Course.query();
        $scope.users = User.query();
        $scope.$watch("enrollment.course",function(newValue,oldValue){
          if(newValue){
            $scope.levels= newValue.levels;
            debugger;
          }else {
            $scope.levels= [];
          }

        });
        $scope.load = function(id) {
            Enrollment.get({id : id}, function(result) {
                $scope.enrollment = result;
                $scope.levels = result.course.levels;

            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:enrollmentUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.enrollment.id != null) {
                Enrollment.update($scope.enrollment, onSaveSuccess, onSaveError);
            } else {
                Enrollment.save($scope.enrollment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

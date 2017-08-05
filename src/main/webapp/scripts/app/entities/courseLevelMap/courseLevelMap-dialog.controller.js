'use strict';

angular.module('buenOjoApp').controller('CourseLevelMapDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CourseLevelMap', 'Course', 'Level',
        function($scope, $stateParams, $modalInstance, entity, CourseLevelMap, Course, Level) {

        $scope.courseLevelMap = entity;
        $scope.courses = Course.query();
        $scope.levels = Level.query();
        $scope.load = function(id) {
            CourseLevelMap.get({id : id}, function(result) {
                $scope.courseLevelMap = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:courseLevelMapUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.courseLevelMap.id != null) {
                CourseLevelMap.update($scope.courseLevelMap, onSaveSuccess, onSaveError);
            } else {
                CourseLevelMap.save($scope.courseLevelMap, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

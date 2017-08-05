'use strict';

angular.module('buenOjoApp').controller('CourseDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Course', 'Level', 'ImageResource',
        function($scope, $stateParams, $modalInstance, entity, Course, Level, ImageResource) {

        $scope.course = entity;
        $scope.levels = Level.query();
        $scope.imageResources = ImageResource.query();
        $scope.load = function(id) {
            Course.get({id : id}, function(result) {
                $scope.course = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:courseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.course.id != null) {
                Course.update($scope.course, onSaveFinished);
            } else {
                Course.save($scope.course, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

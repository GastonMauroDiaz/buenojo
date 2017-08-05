'use strict';

angular.module('buenOjoApp').controller('TagDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Tag', 'TagPair', 'Course',
        function($scope, $stateParams, $modalInstance, entity, Tag, TagPair, Course) {

        $scope.tag = entity;
        $scope.tagpairs = TagPair.query();
        $scope.courses = Course.query();
        $scope.load = function(id) {
            Tag.get({id : id}, function(result) {
                $scope.tag = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:tagUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.tag.id != null) {
                Tag.update($scope.tag, onSaveFinished);
            } else {
                Tag.save($scope.tag, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

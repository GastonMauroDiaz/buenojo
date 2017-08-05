'use strict';

angular.module('buenOjoApp').controller('TagPairDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TagPair', 'Tag', 'ImageCompletionSolution',
        function($scope, $stateParams, $modalInstance, entity, TagPair, Tag, ImageCompletionSolution) {

        $scope.tagPair = entity;
        $scope.tags = Tag.query();
        $scope.imagecompletionsolutions = ImageCompletionSolution.query();
        $scope.load = function(id) {
            TagPair.get({id : id}, function(result) {
                $scope.tagPair = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:tagPairUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.tagPair.id != null) {
                TagPair.update($scope.tagPair, onSaveFinished);
            } else {
                TagPair.save($scope.tagPair, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

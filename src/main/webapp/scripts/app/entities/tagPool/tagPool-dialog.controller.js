'use strict';

angular.module('buenOjoApp').controller('TagPoolDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TagPool', 'Tag',
        function($scope, $stateParams, $modalInstance, entity, TagPool, Tag) {

        $scope.tagPool = entity;
        $scope.tags = Tag.query();
        $scope.load = function(id) {
            TagPool.get({id : id}, function(result) {
                $scope.tagPool = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:tagPoolUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.tagPool.id != null) {
                TagPool.update($scope.tagPool, onSaveFinished);
            } else {
                TagPool.save($scope.tagPool, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

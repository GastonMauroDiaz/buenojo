'use strict';

angular.module('buenOjoApp').controller('LoaderTraceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'LoaderTrace',
        function($scope, $stateParams, $modalInstance, entity, LoaderTrace) {

        $scope.loaderTrace = entity;
        $scope.load = function(id) {
            LoaderTrace.get({id : id}, function(result) {
                $scope.loaderTrace = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:loaderTraceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.loaderTrace.id != null) {
                LoaderTrace.update($scope.loaderTrace, onSaveFinished);
            } else {
                LoaderTrace.save($scope.loaderTrace, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

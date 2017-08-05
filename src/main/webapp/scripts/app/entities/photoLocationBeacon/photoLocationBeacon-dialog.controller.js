'use strict';

angular.module('buenOjoApp').controller('PhotoLocationBeaconDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PhotoLocationBeacon',
        function($scope, $stateParams, $modalInstance, entity, PhotoLocationBeacon) {

        $scope.photoLocationBeacon = entity;
        
        $scope.load = function(id) {
            PhotoLocationBeacon.get({id : id}, function(result) {
                $scope.photoLocationBeacon = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationBeaconUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.photoLocationBeacon.id != null) {
                PhotoLocationBeacon.update($scope.photoLocationBeacon, onSaveFinished);
            } else {
                PhotoLocationBeacon.save($scope.photoLocationBeacon, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

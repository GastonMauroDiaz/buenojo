'use strict';

angular.module('buenOjoApp').controller('HangManGameContainerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HangManGameContainer',
        function($scope, $stateParams, $modalInstance, entity, HangManGameContainer) {

        $scope.hangManGameContainer = entity;
        $scope.load = function(id) {
            HangManGameContainer.get({id : id}, function(result) {
                $scope.hangManGameContainer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:hangManGameContainerUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hangManGameContainer.id != null) {
                HangManGameContainer.update($scope.hangManGameContainer, onSaveSuccess, onSaveError);
            } else {
                HangManGameContainer.save($scope.hangManGameContainer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

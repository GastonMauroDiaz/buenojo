'use strict';

angular.module('buenOjoApp').controller('HangManOptionListItemDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'HangManOptionListItem',
        function($scope, $stateParams, $modalInstance, entity, HangManOptionListItem) {

        $scope.hangManOptionListItem = entity;
        $scope.load = function(id) {
            HangManOptionListItem.get({id : id}, function(result) {
                $scope.hangManOptionListItem = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:hangManOptionListItemUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.hangManOptionListItem.id != null) {
                HangManOptionListItem.update($scope.hangManOptionListItem, onSaveSuccess, onSaveError);
            } else {
                HangManOptionListItem.save($scope.hangManOptionListItem, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

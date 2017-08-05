'use strict';

angular.module('buenOjoApp').controller('PhotoLocationSightPairDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PhotoLocationSightPair', 'PhotoLocationExercise',
        function($scope, $stateParams, $modalInstance, entity, PhotoLocationSightPair, PhotoLocationExercise) {

        $scope.photoLocationSightPair = entity;
        $scope.photolocationexercises = PhotoLocationExercise.query();
        $scope.load = function(id) {
            PhotoLocationSightPair.get({id : id}, function(result) {
                $scope.photoLocationSightPair = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationSightPairUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.photoLocationSightPair.id != null) {
                PhotoLocationSightPair.update($scope.photoLocationSightPair, onSaveFinished);
            } else {
                PhotoLocationSightPair.save($scope.photoLocationSightPair, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

'use strict';

angular.module('buenOjoApp').controller('LoadedExerciseDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'LoadedExercise', 'LoaderTrace',
        function($scope, $stateParams, $modalInstance, entity, LoadedExercise, LoaderTrace) {

        $scope.loadedExercise = entity;
        $scope.loadertraces = LoaderTrace.query();
        $scope.load = function(id) {
            LoadedExercise.get({id : id}, function(result) {
                $scope.loadedExercise = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:loadedExerciseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.loadedExercise.id != null) {
                LoadedExercise.update($scope.loadedExercise, onSaveFinished);
            } else {
                LoadedExercise.save($scope.loadedExercise, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

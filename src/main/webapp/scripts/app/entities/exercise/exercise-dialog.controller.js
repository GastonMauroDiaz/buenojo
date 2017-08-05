'use strict';

angular.module('buenOjoApp').controller('ExerciseDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Exercise', 'Level','Course',
        function($scope, $stateParams, $modalInstance, entity, Exercise, Level,Course) {

        $scope.exercise = entity;
        $scope.levels = Level.query();
        $scope.courses = Course.query();

        $scope.load = function(id) {
            Exercise.get({id : id}, function(result) {
                $scope.exercise = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:exerciseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.exercise.id != null) {
                Exercise.update($scope.exercise, onSaveFinished);
            } else {
                Exercise.save($scope.exercise, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

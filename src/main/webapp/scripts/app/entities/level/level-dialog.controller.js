'use strict';

angular.module('buenOjoApp').controller('LevelDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Level', 'Course', 'Exercise','Activity',
        function($scope, $stateParams, $modalInstance, entity, Level, Course, Exercise, Activity) {

        $scope.level = entity;
        $scope.courses = Course.query();
        $scope.exercises = Exercise.query();
        $scope.levels = Level.query();
        $scope.activities = Activity.query();

        $scope.load = function() {
            if ($scope.level.id){
              $scope.level.$promise.then(function(){
                Activity.level({levelId: entity.id}, function(activities){
                  $scope.level.activities = activities;
                });
              });
            }

        };

        $scope.load();

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:levelUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.level.id != null) {
                Level.update($scope.level, onSaveSuccess, onSaveError);
            } else {
                Level.save($scope.level, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

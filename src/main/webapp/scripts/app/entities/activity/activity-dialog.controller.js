'use strict';

angular.module('buenOjoApp').controller('ActivityDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Activity', 'Level', 'PhotoLocationExercise','ImageCompletionExercise','MultipleChoiceExerciseContainer','HangManGameContainer', 'ActivityType','Course',
        function($scope, $stateParams, $modalInstance, $q, entity, Activity, Level, PhotoLocationExercise, ImageCompletionExercise, MultipleChoiceExerciseContainer,HangManGameContainer, ActivityType,Course) {
        $scope.courses = Course.query();
        $scope.exercises = [];

        $scope.activity = entity;
        $scope.fetchAll = function(type){
          switch (ActivityType.fromName(type)) {
            case ActivityType.HangMan:
              return HangManGameContainer.query();
            case ActivityType.MultipleChoice:
              return MultipleChoiceExerciseContainer.query();
            case ActivityType.ImageCompletion:
              return ImageCompletionExercise.query();
            case ActivityType.PhotoLocation:
              return PhotoLocationExercise.query();
            default:
              return []

          }
        }
        $scope.fetchExercise = function(exerciseId,type){
          switch (ActivityType.fromName(type)) {
            case ActivityType.HangMan:
              return HangManGameContainer.get({id: exerciseId});
            case ActivityType.MultipleChoice:
              return MultipleChoiceExerciseContainer.get({id: exerciseId});
            case ActivityType.ImageCompletion:
              return ImageCompletionExercise.get({id: exerciseId});
            case ActivityType.PhotoLocation:
              return PhotoLocationExercise.get({id: exerciseId});
            default:
              return undefined

          }
        }
        $q.all([$scope.activity.$promise, $scope.exercises.$promise]).then(function() {
            if (!$scope.activity.exerciseId) {
                return $q.reject();
            }
            return $scope.fetchExercise($scope.activity.exerciseId, $scope.activity.type).$promise;
        }).then(function(exercise) {
            $scope.activity.exercise = exercise;
            $scope.exercises.push($scope.fetchAll($scope.activity.type));
        });

        $scope.$watch("activity.type",function(newValue,oldValue){
            $scope.exercises = $scope.fetchAll(newValue);
            $scope.activity.exercise = undefined;
        });

        $scope.levels = Level.query();

        $scope.load = function(id) {
            Activity.get({id : id}, function(result) {
                $scope.activity = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:activityUpdate', result);
            $modalInstance.close(result);
        };
        $scope.resolveLevel = function(level){
          if (isFinite(level.course)){
            var course = $scope.courses.find(function(element,index,array) {
                return element.id === level.course;
            });

            level.course = course;
          }

          return level;
        }
        $scope.save = function () {
            var activity = {
              id: $scope.activity.id,
              type: $scope.activity.type,
              exercise: $scope.activity.exercise.id,
              level: $scope.resolveLevel($scope.activity.level)
            };
            if ($scope.activity.id != null) {

                Activity.update(activity, onSaveFinished);
            } else {
                Activity.save(activity, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

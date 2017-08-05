'use strict';

angular.module('buenOjoApp')
    .controller('ActivityController', function ($scope, $state, Activity, ParseLinks, ActivityType, Level) {
        $scope.activities = [];
        $scope.page = 0;

        $scope.levels = Level.query();
        $scope.resolveLevel = function (activity){
          if (activity.level && isFinite(activity.level)){
            activity.level = $scope.levels.find(function(element,index,array){
              return element.id === activity.level;
            });
            return activity.level.name;
          } else if (activity.level) {
            return activity.level.name;
          }


        }
        $scope.loadAll = function() {
            Activity.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.activities = result;
            });
        };
        $scope.goToExercise = function(activity) {
          ActivityType.goToExerciseDetail(activity)
        };
        // $scope.goToExercise = function(activity) {
        //    switch(ActivityType.fromName(activity.type)){
        //     case ActivityType.HangMan:
        //         $state.go('imageCompletionExercise.detail', {id: activity.exerciseId});
        //       break;
        //     case ActivityType.MultipleChoice:
        //         $state.go('multipleChoiceExerciseContainer.detail', {id: activity.exerciseId});
        //       break;
        //     case ActivityType.PhotoLocation:
        //         $state.go('photoLocationExercise.detail', {id: activity.exerciseId});
        //       break;
        //     case ActivityType.ImageCompletion:
        //         $state.go('imageCompletionExercise.detail',{id: activity.exerciseId});
        //       break;
        //     default:
        //       return;
        //   }
        // };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Activity.get({id: id}, function(result) {
                $scope.activity = result;
                $('#deleteActivityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Activity.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteActivityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.activity = {
                type: null,
                id: null
            };
        };
    });

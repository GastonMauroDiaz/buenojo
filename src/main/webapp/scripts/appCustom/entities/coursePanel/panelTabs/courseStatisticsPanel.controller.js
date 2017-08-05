'use strict';

angular.module('buenOjoApp')
    .controller('CourseStatisticsPanelController', function ($scope,$stateParams, Course, Enrollment, BuenOjoUtils,ActivityType, CourseStatistics) {
        $scope.timeByExercise = [];

        CourseStatistics.averageTimeByExerciseType({courseId: $stateParams.id}, function(result){
          $scope.timeByExercise = result;
        })
        // [
        //   {
        //     exerciseType: 'HangMan',
        //     duration: 523
        //   },
        //   {
        //     exerciseType: 'MultipleChoice',
        //     duration: 256
        //   },
        //   {
        //     exerciseType: 'ImageCompletion',
        //     duration: 103
        //   },{
        //     exerciseType: 'PhotoLocation',
        //     duration: 223
        //   }
        // ];

        // $scope.scoreByExercise = [
        //     {
        //       exerciseType: 'HangMan',
        //       score: 3.2
        //     },
        //     {
        //       exerciseType: 'MultipleChoice',
        //       score: 4.3
        //     },
        //     {
        //       exerciseType: 'ImageCompletion',
        //       score: 2.3
        //     },{
        //       exerciseType: 'PhotoLocation',
        //       score: 2.152
        //     }
        //   ];

        $scope.scoreByExercise = [];
        CourseStatistics.averageScoreByExerciseType(
              {
                courseId: $stateParams.id
              },
            function(result){
              $scope.scoreByExercise = result;
            });
          $scope.describe = function(type){
            return ActivityType.describe(type);
          };
          $scope.inMinutes = function(seconds){
            return BuenOjoUtils.secondsToMinutes(seconds);
          }
    });

angular.module('buenOjoApp')
    .service('CourseStatistics', function($resource) {
      return $resource('', {}, {

          'averageScoreByExerciseType': {
              method: 'GET',
              transformResponse: function (data) {
                  data = angular.fromJson(data);
                  return data;
              },
              isArray: true,
              url: 'api/courseSatistics/:courseId/averageScoreByExerciseType'
          },
          'averageTimeByExerciseType': {
              method: 'GET',
              transformResponse: function (data) {
                  data = angular.fromJson(data);
                  return data;
              },
              isArray: true,
              url: 'api/courseSatistics/:courseId/timeByExerciseType'
          },

      });
    });

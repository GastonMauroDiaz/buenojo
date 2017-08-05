'use strict';

angular.module('buenOjoApp')
    .factory('ExerciseDataSet', function ($resource, DateUtils) {
        return $resource('api/dataSets', {}, {

            'query': { method: 'GET', isArray: true},
            'load': { method: 'POST' }

        });
      });

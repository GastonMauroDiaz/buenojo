'use strict';

angular.module('buenOjoApp')
    .factory('PhotoLocationExercise', function ($resource, DateUtils) {
        return $resource('api/photoLocationExercises/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
              },
            'any': {
              method: 'GET',
              transformResponse: function (data) {
                              data = angular.fromJson(data);
                              return data;
                            },
              url: 'api/photoLocationExercises/any'
            },
            'update': { method:'PUT' }
        });
    });

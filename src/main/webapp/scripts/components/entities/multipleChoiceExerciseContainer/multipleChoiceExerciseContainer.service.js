'use strict';

angular.module('buenOjoApp')
    .factory('MultipleChoiceExerciseContainer', function ($resource, DateUtils) {
        return $resource('api/multipleChoiceExerciseContainers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

'use strict';

angular.module('buenOjoApp')
    .factory('HangManExerciseOption', function ($resource, DateUtils) {
        return $resource('api/hangManExerciseOptions/:id', {}, {
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

angular.module('buenOjoApp')
.factory('HangManExerciseOptionByContainer', function ($resource, DateUtils) {
    return $resource('api/hangManExerciseOptions/container/:id', {}, {
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

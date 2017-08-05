'use strict';

angular.module('buenOjoApp')
    .factory('HangManExerciseImageResource', function ($resource, DateUtils) {
        return $resource('api/hangManExerciseImageResources/:id', {}, {
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
.factory('HangManExerciseImageResourceByContainer', function ($resource, DateUtils) {
    return $resource('api/hangManExercisesImageResources/container/:id', {}, {
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

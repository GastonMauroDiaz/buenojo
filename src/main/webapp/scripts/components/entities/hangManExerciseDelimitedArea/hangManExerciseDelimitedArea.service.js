'use strict';

angular.module('buenOjoApp')
    .factory('HangManExerciseDelimitedArea', function ($resource, DateUtils) {
        return $resource('api/hangManExerciseDelimitedAreas/:id', {}, {
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
.factory('HangManExerciseDelimitedAreaByContainer', function ($resource, DateUtils) {
    return $resource('api/hangManDelimitedAreas/container/:id', {}, {
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


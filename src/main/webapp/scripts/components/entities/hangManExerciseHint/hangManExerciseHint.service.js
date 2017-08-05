'use strict';

angular.module('buenOjoApp')
    .factory('HangManExerciseHint', function ($resource, DateUtils) {
        return $resource('api/hangManExerciseHints/:id', {}, {
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
.factory('HangManExerciseHintByContainer', function ($resource, DateUtils) {
    return $resource('api/hangManExerciseHints/container/:id', {}, {
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

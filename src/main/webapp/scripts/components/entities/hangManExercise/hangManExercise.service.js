'use strict';

angular.module('buenOjoApp')
    .factory('HangManExercise', function ($resource, DateUtils) {
        return $resource('api/hangManExercises/:id', {}, {
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
.factory('HangManExerciseByContainer', function ($resource, DateUtils) {
    return $resource('api/hangManExercises/container/:id', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            isArray: true,
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },
        'update': { method:'PUT' }
    });
});

angular.module('buenOjoApp')
.factory('HangManExerciseIsCorrect', function ($resource, DateUtils) {
    return $resource('api/hangManExercises/:id/:responses', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            isArray: true,
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },
        'update': { method:'PUT' }
    });
});

angular.module('buenOjoApp')
.factory('HangManExerciseWhichAreIncorrect', function ($resource, DateUtils) {
    return $resource('api/hangManExercises/getIncorrects/:id/:responses', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            isArray: true,
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },
        'update': { method:'PUT' }
    });
});

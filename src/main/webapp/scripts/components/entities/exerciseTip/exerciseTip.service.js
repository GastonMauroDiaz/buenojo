'use strict';

angular.module('buenOjoApp')
    .factory('ExerciseTip', function ($resource, DateUtils) {
        return $resource('api/exerciseTips/:id', {}, {
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

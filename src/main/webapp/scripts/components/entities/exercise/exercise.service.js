'use strict';

angular.module('buenOjoApp')
    .factory('Exercise', function ($resource, DateUtils) {
        return $resource('api/exercises/:id', {}, {
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

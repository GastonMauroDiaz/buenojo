'use strict';

angular.module('buenOjoApp')
    .factory('PhotoLocationKeyword', function ($resource, DateUtils) {
        return $resource('api/photoLocationKeywords/:id', {}, {
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

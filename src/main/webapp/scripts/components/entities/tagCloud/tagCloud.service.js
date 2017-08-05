'use strict';

angular.module('buenOjoApp')
    .factory('TagCloud', function ($resource, DateUtils) {
        return $resource('api/tagCloud/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET', isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
           
        });
    });
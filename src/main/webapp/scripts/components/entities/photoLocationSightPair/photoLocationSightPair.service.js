'use strict';

angular.module('buenOjoApp')
    .factory('PhotoLocationSightPair', function ($resource, DateUtils) {
        return $resource('api/photoLocationSightPairs/:id', {}, {
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

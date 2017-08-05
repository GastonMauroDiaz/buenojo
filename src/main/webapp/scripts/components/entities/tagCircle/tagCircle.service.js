'use strict';

angular.module('buenOjoApp')
    .factory('TagCircle', function ($resource, DateUtils) {
        return $resource('api/tagCircles/:id', {}, {
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

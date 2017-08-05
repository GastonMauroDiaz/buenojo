'use strict';

angular.module('buenOjoApp')
    .factory('CourseInformation', function ($resource, DateUtils) {
        return $resource('api/courseInformation/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'user': {
                method: 'GET',
                url: 'api/courseInformation/:id/user/:userId'
            }
        });
    });

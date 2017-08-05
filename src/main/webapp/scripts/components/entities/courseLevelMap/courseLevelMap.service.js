'use strict';

angular.module('buenOjoApp')
    .factory('CourseLevelMap', function ($resource, DateUtils) {
        return $resource('api/courseLevelMaps/:id', {}, {
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
.factory('CourseLevelMapGraph', function ($resource, DateUtils) {
    return $resource('api/courseLevelMaps/course/:courseId', {courseId: '@id'}, {
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


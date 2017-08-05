'use strict';

angular.module('buenOjoApp')
    .factory('Enrollment', function ($resource, DateUtils) {
        return $resource('api/enrollments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'courseAndUser': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                },
                url: 'api/enrollments/user/:userId/course/:courseId'
            }

        });
    });

angular.module('buenOjoApp')
.factory('EnrollmentCurrentUser', function ($resource, DateUtils) {
    return $resource('api/enrollmentsCurrentUser', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },
        'update': { method:'PUT' },
        'finish': {
            method: 'PUT',
            url: 'api/enrollments/finish'
        }
    });
});
angular.module('buenOjoApp')
.factory('EnrollmentCurrentUserAndCourse', function ($resource, DateUtils) {
    return $resource('api/enrollmentCurrentUser/:id', {}, {

        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },

    });
});

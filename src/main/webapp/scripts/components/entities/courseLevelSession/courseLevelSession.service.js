'use strict';

angular.module('buenOjoApp')
    .factory('CourseLevelSession', function ($resource, DateUtils) {
        return $resource('api/courseLevelSessions/:id', {}, {
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
.factory('CourseLevelSessionFiltered', function ($resource, DateUtils) {
    return $resource('api/courseLevelSessionCurrentUserAndCourse/:courseId', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },
        'update': { method:'PUT' },
        'current': {
            method: 'GET',
            url: 'api/currentCourseLevelSessionCurrentUserAndCourse/:courseId',
            transformResponse: function (data) {
              data = angular.fromJson(data);
              return data;
              }
        },
        'userAndCourse': {
          method: 'GET',
          isArray: true,
          url: 'api/courseLevelSessions/user/:userId/course/:courseId'
        }
    });
});

angular.module('buenOjoApp')
.factory('CourseLevelSessionAddScore', function ($resource, DateUtils) {
    return $resource('api/courseLevelSessions/:id/add/:points/:experience', { }, {

        'update': { method:'GET' }
    });
});

angular.module('buenOjoApp')
.factory('CourseLevelSessionEnforceEnrollment', function ($resource, DateUtils) {
    return $resource('api/enforceEnrollment/:courseId',{}, {
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
.factory('CourseLevelSessionEnforceEnrollmentRandom', function ($resource, DateUtils) {
    return $resource('api/enforceRandomEnrollment/:courseId', {}, {
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

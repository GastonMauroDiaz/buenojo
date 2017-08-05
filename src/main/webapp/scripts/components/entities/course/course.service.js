'use strict';

angular.module('buenOjoApp')
    .factory('Course', function($resource, DateUtils) {
        return $resource('api/courses/:id', {}, {
            'query': {
                method: 'GET',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function(data) {
                    data = angular.fromJson(data);
                    data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                    data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT'
            },
            'enrollments': {
                method: 'GET',
                isArray: true,
                url: 'api/courses/:id/enrollments'
            }
        });
    });


angular.module('buenOjoApp')
    .factory('CourseUtils', function() {
        return {

            statusForCourse: function(course) {
                if (!course) return "";
                if (course.courseOpen) {
                    return "Abierto";
                } else {
                    return "Cerrado";
                }
            },

            translateEnrollmentStatusToAction: function(status) {

                switch (status) {
                    case 'Started':
                    case 'InProgress':
                        return 'EN PROGRESO';
                        break;
                    case 'Cancelled':
                    case undefined:
                        return 'COMENZAR';
                        break;
                    case 'Finished':
                        return 'FINALIZADO';
                        break;
                }
            }

        };
    });

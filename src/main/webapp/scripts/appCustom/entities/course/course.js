'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('courseslist', {
                parent: 'entity',
                url: '/courseslist',
                data: {
                  authorities: ['ROLE_COURSE_STUDENT'],
                    pageTitle: 'Cursos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/course/courses.html',
                        controller: 'CourseControllerList'
                    }
                },
                params: {
                  force: false
                },
                resolve: {
                }
            })
            .state('course.graph', {
                parent: 'entity',
                url: '/courseLevelMapGraph/{id}',
                data: {
                    authorities: ['ROLE_COURSE_STUDENT'],
                    pageTitle: 'Cursos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/courseLevelMapGraph/courseLevelMapGraph.html',
                        controller: 'CourseLevelMapGraphController'
                    }
                },
                resolve: {

                }
            })

    });

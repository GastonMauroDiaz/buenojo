'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('courseLevelMapGraph', {
                parent: 'entity',
                url: '/courseLevelMapsGraph',
                data: {
                    authorities: ['ROLE_USER','ROLE_COURSE_STUDENT'],
                    pageTitle: 'CourseLevelMapsGraph'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/courseLevelMapGraph/courseLevelMaps.html',
                        controller: 'CourseLevelMapGraphController'
                    }
                },
                resolve: {
                }
            })
                });

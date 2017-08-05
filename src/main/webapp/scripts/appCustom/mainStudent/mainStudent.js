'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('homeStudent', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: ['ROLE_COURSE_STUDENT']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/mainStudent/mainStudent.html',
                        controller: 'MainStudentController'
                    }
                },
                resolve: {
                    
                }
            });
    });

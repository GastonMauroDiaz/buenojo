'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('homeAdmin', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: ['ROLE_COURSE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/mainAdmin/mainAdmin.html',
                        controller: 'MainAdminController'
                    }
                },
                resolve: {
                    
                }
            });
    });

'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('controlPanel', {
                parent: 'entity',
                url: '/controlPanel',
                data: {
                    authorities: ['ROLE_COURSE_ADMIN'],
                    pageTitle: 'BuenOjo - Control Panel'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/controlPanel/controlPanel.html',
                        controller: 'ControlPanelController'
                    }
                },
                resolve: {
                }
            }); 
         
            
            
         
    });

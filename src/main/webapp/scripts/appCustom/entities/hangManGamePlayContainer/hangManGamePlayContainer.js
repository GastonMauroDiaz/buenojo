'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
        .state('hangManGamePlayContainerList', {
            parent: 'entity',
            url: '/hangManGameContainers',
            data: {
                authorities:  ['ROLE_COURSE_STUDENT'],
                pageTitle: 'HangManGameContainers'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/appCustom/entities/hangManGamePlayContainer/hangManGameContainers.html',
                    controller: 'HangManGameContainerController'
                }
            },
            resolve: {
            }
        })
         .state('hangManGameContainer.exercises', {
                parent: 'entity',
                url: '/hangManGameContainerExercises/{id}',
                data: {
                    authorities: ['ROLE_COURSE_STUDENT'],
                    pageTitle: 'HangManGameContainer'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExercise/hangManExercises.html',
                        controller: 'HangManGameContainerDetailExerciseController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManGameContainer', function($stateParams, HangManGameContainer) {
                        return HangManGameContainer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hangManGamePlayContainer', {
                parent: 'entity',
                url: '/hangManGamePlayContainer/{id}',
                data: {
                    authorities: ['ROLE_COURSE_STUDENT'],
                    pageTitle: 'HangManGameContainers'
                },
                params: {
                  activity: null, enrollment:null
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/hangManGamePlayContainer/hangManGamePlayContainer.html',
                        controller: 'HangManGamePlayContainerController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManGameContainer', function($stateParams, HangManGameContainer) {
                        return HangManGameContainer.get({id : $stateParams.id});
                    }]
                }
            });
    });

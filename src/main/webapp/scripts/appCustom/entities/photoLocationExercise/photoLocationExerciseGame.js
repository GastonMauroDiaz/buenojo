'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationExerciseGame', {
                parent: 'entity',
                url: '/photoLocationExerciseGame/{id}',
                data: {
                    authorities: ['ROLE_COURSE_STUDENT'],
                    pageTitle: 'Play exercise'
                },
                params: {
                  enrollment: null,
                  activity: null
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/photoLocationExercise/photoLocationExerciseGame.html',
                        controller: 'PhotoLocationExerciseGameController'
                    }

                },
                resolve: {
                	   entity: ['$stateParams', 'PhotoLocationExercise', function($stateParams, PhotoLocationExercise) {
                           return PhotoLocationExercise.get({id : $stateParams.id});
                       }]
                }
            })
            .state('photoLocationExerciseList', {
                parent: 'entity',
                url: '/photoLocationExerciseList',
                data: {
                    authorities:  ['ROLE_DEMO_USER','ROLE_COURSE_STUDENT'],
                    pageTitle: 'Ejercicios de Fotolocalización'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/photoLocationExercise/photoLocationExerciseList.html',
                        controller: 'PhotoLocationExerciseListController'
                    }
                },
                resolve: {
                  entity: ['$stateParams', 'PhotoLocationExercise', function($stateParams, PhotoLocationExercise) {
                        return PhotoLocationExercise.query();
                    }]
                }
            });
    });

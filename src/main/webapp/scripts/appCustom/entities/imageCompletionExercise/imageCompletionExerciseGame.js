'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('imageCompletionExerciseGame', {
                parent: 'entity',
                url: '/imageCompletionExerciseGame/{id}',
                data: {
                    authorities: ['ROLE_COURSE_STUDENT','ROLE_DEMO_USER'],
                    pageTitle: 'Play exercise'
                },
                params: {
                  activity: null,
                  enrollment:null,
                  id: null
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/imageCompletionExercise/imageCompletionExerciseGame.html',
                        controller: 'ImageCompletionExerciseGameController'
                    },
                    'tip@imageCompletionExerciseGame': {
                    	templateUrl: 'scripts/appCustom/entities/exerciseTip/exerciseTip-view.html',
                    	controller: 'ExerciseTipViewController',
                      resolve: {
                        entity: ['ExerciseTip', function(ExerciseTip) {
                            return ExerciseTip.get({id : $stateParams.id});
                        }]
                      }
                    },
                    'map-view@imageCompletionExerciseGame': {
                        templateUrl: 'scripts/appCustom/entities/imageCompletionExercise/ice-mapview.html',
                        controller: 'ICEMapViewController'
                    }
                },
                resolve: {
                	   entity: ['$stateParams', 'ImageCompletionExercise', function($stateParams, ImageCompletionExercise) {
                           return ImageCompletionExercise.get({id : $stateParams.id});
                       }]
                }
            })
            .state('imageCompletionExerciseList', {
                parent: 'entity',
                url: '/imageCompletionExerciseList',
                data: {
                    authorities:  ['ROLE_DEMO_USER','ROLE_COURSE_STUDENT'],
                    pageTitle: 'Arrastrar y Soltar'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/imageCompletionExercise/imageCompletionExerciseList.html',
                        controller: 'ImageCompletionExerciseListController'
                    }
                },
                resolve: {
                }
            });
    });

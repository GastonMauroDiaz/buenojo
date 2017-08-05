'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceExerciseContainerList', {
                parent: 'entity',
                url: '/multipleChoiceExerciseContainersList',
                data: {
                    authorities: [],
                    pageTitle: 'BuenOjo - Selección de set de preguntas MultipleChoice'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/multipleChoiceExerciseContainer/multipleChoiceExerciseContainers.html',
                        controller: 'MultipleChoiceExerciseContainerListController'
                    }
                },
                resolve: {
                }
            }) .state('multipleChoiceExerciseContainerPlay', {
                parent: 'entity',
                url: '/multipleChoiceExerciseContainerPlay/{id}',
                params: {
                  enrollment: null,
                  activity: null
                },
                data: {
                    authorities: [],
                    pageTitle: 'BuenOjo - Preguntas MultipleChoice'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/multipleChoiceExerciseContainer/multipleChoiceExerciseContainerPlay.html',
                        controller: 'MultipleChoiceExerciseContainerPlayController'
                    }
                },
                resolve: {
                }
            }).state('multipleChoiceExerciseContainerPlayWithSession', {
                parent: 'entity',
                url: '/multipleChoiceExerciseContainerPlayWithSession/{id}',
                data: {
                    authorities: [],
                    pageTitle: 'BuenOjo - Preguntas MultipleChoice',
                    withSession: true
                },
                params: {
                  enrollment: null,
                  activity: null
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/multipleChoiceExerciseContainer/multipleChoiceExerciseContainerPlay.html',
                        controller: 'MultipleChoiceExerciseContainerPlayController'
                    }
                },

            });




    });

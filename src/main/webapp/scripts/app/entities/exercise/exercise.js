'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('exercise', {
                parent: 'entity',
                url: '/exercises',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Exercises'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/exercise/exercises.html',
                        controller: 'ExerciseController'
                    }
                },
                resolve: {
                }
            })
            .state('exercise.detail', {
                parent: 'entity',
                url: '/exercise/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Exercise'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/exercise/exercise-detail.html',
                        controller: 'ExerciseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Exercise', function($stateParams, Exercise) {
                        return Exercise.get({id : $stateParams.id});
                    }]
                }
            })
            .state('exercise.new', {
                parent: 'exercise',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/exercise/exercise-dialog.html',
                        controller: 'ExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    totalScore: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('exercise', null, { reload: true });
                    }, function() {
                        $state.go('exercise');
                    })
                }]
            })
            .state('exercise.edit', {
                parent: 'exercise',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/exercise/exercise-dialog.html',
                        controller: 'ExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Exercise', function(Exercise) {
                                return Exercise.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('exercise', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

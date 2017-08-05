'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('loadedExercise', {
                parent: 'entity',
                url: '/loadedExercises',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'LoadedExercises'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/loadedExercise/loadedExercises.html',
                        controller: 'LoadedExerciseController'
                    }
                },
                resolve: {
                }
            })
            .state('loadedExercise.detail', {
                parent: 'entity',
                url: '/loadedExercise/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'LoadedExercise'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/loadedExercise/loadedExercise-detail.html',
                        controller: 'LoadedExerciseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'LoadedExercise', function($stateParams, LoadedExercise) {
                        return LoadedExercise.get({id : $stateParams.id});
                    }]
                }
            })
            .state('loadedExercise.new', {
                parent: 'loadedExercise',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/loadedExercise/loadedExercise-dialog.html',
                        controller: 'LoadedExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    exerciseId: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('loadedExercise', null, { reload: true });
                    }, function() {
                        $state.go('loadedExercise');
                    })
                }]
            })
            .state('loadedExercise.edit', {
                parent: 'loadedExercise',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/loadedExercise/loadedExercise-dialog.html',
                        controller: 'LoadedExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LoadedExercise', function(LoadedExercise) {
                                return LoadedExercise.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('loadedExercise', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

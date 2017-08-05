'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hangManExercise', {
                parent: 'entity',
                url: '/hangManExercises',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExercises'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExercise/hangManExercises.html',
                        controller: 'HangManExerciseController'
                    }
                },
                resolve: {
                }
            })
            .state('hangManExercise.detail', {
                parent: 'entity',
                url: '/hangManExercise/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExercise'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExercise/hangManExercise-detail.html',
                        controller: 'HangManExerciseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManExercise', function($stateParams, HangManExercise) {
                        return HangManExercise.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hangManExercise.new', {
                parent: 'hangManExercise',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExercise/hangManExercise-dialog.html',
                        controller: 'HangManExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    task: null,
                                    exerciseOrder: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExercise', null, { reload: true });
                    }, function() {
                        $state.go('hangManExercise');
                    })
                }]
            })
            .state('hangManExercise.edit', {
                parent: 'hangManExercise',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExercise/hangManExercise-dialog.html',
                        controller: 'HangManExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HangManExercise', function(HangManExercise) {
                                return HangManExercise.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExercise', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hangManExercise.delete', {
                parent: 'hangManExercise',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExercise/hangManExercise-delete-dialog.html',
                        controller: 'HangManExerciseDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HangManExercise', function(HangManExercise) {
                                return HangManExercise.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExercise', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

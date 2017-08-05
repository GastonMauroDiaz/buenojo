'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hangManExerciseHint', {
                parent: 'entity',
                url: '/hangManExerciseHints',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExerciseHints'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExerciseHint/hangManExerciseHints.html',
                        controller: 'HangManExerciseHintController'
                    }
                },
                resolve: {
                }
            })
            .state('hangManExerciseHint.detail', {
                parent: 'entity',
                url: '/hangManExerciseHint/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExerciseHint'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExerciseHint/hangManExerciseHint-detail.html',
                        controller: 'HangManExerciseHintDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManExerciseHint', function($stateParams, HangManExerciseHint) {
                        return HangManExerciseHint.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hangManExerciseHint.new', {
                parent: 'hangManExerciseHint',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseHint/hangManExerciseHint-dialog.html',
                        controller: 'HangManExerciseHintDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    text: null,
                                    x: null,
                                    y: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseHint', null, { reload: true });
                    }, function() {
                        $state.go('hangManExerciseHint');
                    })
                }]
            })
            .state('hangManExerciseHint.edit', {
                parent: 'hangManExerciseHint',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseHint/hangManExerciseHint-dialog.html',
                        controller: 'HangManExerciseHintDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HangManExerciseHint', function(HangManExerciseHint) {
                                return HangManExerciseHint.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseHint', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hangManExerciseHint.delete', {
                parent: 'hangManExerciseHint',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseHint/hangManExerciseHint-delete-dialog.html',
                        controller: 'HangManExerciseHintDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HangManExerciseHint', function(HangManExerciseHint) {
                                return HangManExerciseHint.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseHint', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hangManExerciseOption', {
                parent: 'entity',
                url: '/hangManExerciseOptions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExerciseOptions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExerciseOption/hangManExerciseOptions.html',
                        controller: 'HangManExerciseOptionController'
                    }
                },
                resolve: {
                }
            })
            .state('hangManExerciseOption.detail', {
                parent: 'entity',
                url: '/hangManExerciseOption/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExerciseOption'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExerciseOption/hangManExerciseOption-detail.html',
                        controller: 'HangManExerciseOptionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManExerciseOption', function($stateParams, HangManExerciseOption) {
                        return HangManExerciseOption.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hangManExerciseOption.new', {
                parent: 'hangManExerciseOption',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseOption/hangManExerciseOption-dialog.html',
                        controller: 'HangManExerciseOptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    text: null,
                                    isCorrect: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseOption', null, { reload: true });
                    }, function() {
                        $state.go('hangManExerciseOption');
                    })
                }]
            })
            .state('hangManExerciseOption.edit', {
                parent: 'hangManExerciseOption',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseOption/hangManExerciseOption-dialog.html',
                        controller: 'HangManExerciseOptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HangManExerciseOption', function(HangManExerciseOption) {
                                return HangManExerciseOption.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseOption', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hangManExerciseOption.delete', {
                parent: 'hangManExerciseOption',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseOption/hangManExerciseOption-delete-dialog.html',
                        controller: 'HangManExerciseOptionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HangManExerciseOption', function(HangManExerciseOption) {
                                return HangManExerciseOption.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseOption', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

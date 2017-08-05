'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hangManExerciseImageResource', {
                parent: 'entity',
                url: '/hangManExerciseImageResources',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExerciseImageResources'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExerciseImageResource/hangManExerciseImageResources.html',
                        controller: 'HangManExerciseImageResourceController'
                    }
                },
                resolve: {
                }
            })
            .state('hangManExerciseImageResource.detail', {
                parent: 'entity',
                url: '/hangManExerciseImageResource/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExerciseImageResource'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExerciseImageResource/hangManExerciseImageResource-detail.html',
                        controller: 'HangManExerciseImageResourceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManExerciseImageResource', function($stateParams, HangManExerciseImageResource) {
                        return HangManExerciseImageResource.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hangManExerciseImageResource.new', {
                parent: 'hangManExerciseImageResource',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseImageResource/hangManExerciseImageResource-dialog.html',
                        controller: 'HangManExerciseImageResourceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseImageResource', null, { reload: true });
                    }, function() {
                        $state.go('hangManExerciseImageResource');
                    })
                }]
            })
            .state('hangManExerciseImageResource.edit', {
                parent: 'hangManExerciseImageResource',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseImageResource/hangManExerciseImageResource-dialog.html',
                        controller: 'HangManExerciseImageResourceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HangManExerciseImageResource', function(HangManExerciseImageResource) {
                                return HangManExerciseImageResource.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseImageResource', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hangManExerciseImageResource.delete', {
                parent: 'hangManExerciseImageResource',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseImageResource/hangManExerciseImageResource-delete-dialog.html',
                        controller: 'HangManExerciseImageResourceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HangManExerciseImageResource', function(HangManExerciseImageResource) {
                                return HangManExerciseImageResource.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseImageResource', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

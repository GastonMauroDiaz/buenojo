'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hangManExerciseDelimitedArea', {
                parent: 'entity',
                url: '/hangManExerciseDelimitedAreas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExerciseDelimitedAreas'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExerciseDelimitedArea/hangManExerciseDelimitedAreas.html',
                        controller: 'HangManExerciseDelimitedAreaController'
                    }
                },
                resolve: {
                }
            })
            .state('hangManExerciseDelimitedArea.detail', {
                parent: 'entity',
                url: '/hangManExerciseDelimitedArea/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManExerciseDelimitedArea'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManExerciseDelimitedArea/hangManExerciseDelimitedArea-detail.html',
                        controller: 'HangManExerciseDelimitedAreaDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManExerciseDelimitedArea', function($stateParams, HangManExerciseDelimitedArea) {
                        return HangManExerciseDelimitedArea.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hangManExerciseDelimitedArea.new', {
                parent: 'hangManExerciseDelimitedArea',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseDelimitedArea/hangManExerciseDelimitedArea-dialog.html',
                        controller: 'HangManExerciseDelimitedAreaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    x: null,
                                    y: null,
                                    radius: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseDelimitedArea', null, { reload: true });
                    }, function() {
                        $state.go('hangManExerciseDelimitedArea');
                    })
                }]
            })
            .state('hangManExerciseDelimitedArea.edit', {
                parent: 'hangManExerciseDelimitedArea',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseDelimitedArea/hangManExerciseDelimitedArea-dialog.html',
                        controller: 'HangManExerciseDelimitedAreaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HangManExerciseDelimitedArea', function(HangManExerciseDelimitedArea) {
                                return HangManExerciseDelimitedArea.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseDelimitedArea', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hangManExerciseDelimitedArea.delete', {
                parent: 'hangManExerciseDelimitedArea',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManExerciseDelimitedArea/hangManExerciseDelimitedArea-delete-dialog.html',
                        controller: 'HangManExerciseDelimitedAreaDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HangManExerciseDelimitedArea', function(HangManExerciseDelimitedArea) {
                                return HangManExerciseDelimitedArea.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManExerciseDelimitedArea', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

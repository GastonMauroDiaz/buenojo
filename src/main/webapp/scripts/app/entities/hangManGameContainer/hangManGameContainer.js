'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hangManGameContainer', {
                parent: 'entity',
                url: '/hangManGameContainers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManGameContainers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManGameContainer/hangManGameContainers.html',
                        controller: 'HangManGameContainerController'
                    }
                },
                resolve: {
                }
            })
            .state('hangManGameContainer.detail', {
                parent: 'entity',
                url: '/hangManGameContainer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManGameContainer'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManGameContainer/hangManGameContainer-detail.html',
                        controller: 'HangManGameContainerDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManGameContainer', function($stateParams, HangManGameContainer) {
                        return HangManGameContainer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hangManGameContainer.new', {
                parent: 'hangManGameContainer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManGameContainer/hangManGameContainer-dialog.html',
                        controller: 'HangManGameContainerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hangManGameContainer', null, { reload: true });
                    }, function() {
                        $state.go('hangManGameContainer');
                    })
                }]
            })
            .state('hangManGameContainer.edit', {
                parent: 'hangManGameContainer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManGameContainer/hangManGameContainer-dialog.html',
                        controller: 'HangManGameContainerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HangManGameContainer', function(HangManGameContainer) {
                                return HangManGameContainer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManGameContainer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hangManGameContainer.delete', {
                parent: 'hangManGameContainer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManGameContainer/hangManGameContainer-delete-dialog.html',
                        controller: 'HangManGameContainerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HangManGameContainer', function(HangManGameContainer) {
                                return HangManGameContainer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManGameContainer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

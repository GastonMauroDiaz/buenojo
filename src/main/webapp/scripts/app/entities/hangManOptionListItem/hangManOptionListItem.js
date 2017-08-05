'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('hangManOptionListItem', {
                parent: 'entity',
                url: '/hangManOptionListItems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManOptionListItems'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManOptionListItem/hangManOptionListItems.html',
                        controller: 'HangManOptionListItemController'
                    }
                },
                resolve: {
                }
            })
            .state('hangManOptionListItem.detail', {
                parent: 'entity',
                url: '/hangManOptionListItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'HangManOptionListItem'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/hangManOptionListItem/hangManOptionListItem-detail.html',
                        controller: 'HangManOptionListItemDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'HangManOptionListItem', function($stateParams, HangManOptionListItem) {
                        return HangManOptionListItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('hangManOptionListItem.new', {
                parent: 'hangManOptionListItem',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManOptionListItem/hangManOptionListItem-dialog.html',
                        controller: 'HangManOptionListItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    optionGroup: null,
                                    optionType: null,
                                    optionText: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('hangManOptionListItem', null, { reload: true });
                    }, function() {
                        $state.go('hangManOptionListItem');
                    })
                }]
            })
            .state('hangManOptionListItem.edit', {
                parent: 'hangManOptionListItem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManOptionListItem/hangManOptionListItem-dialog.html',
                        controller: 'HangManOptionListItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['HangManOptionListItem', function(HangManOptionListItem) {
                                return HangManOptionListItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManOptionListItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('hangManOptionListItem.delete', {
                parent: 'hangManOptionListItem',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/hangManOptionListItem/hangManOptionListItem-delete-dialog.html',
                        controller: 'HangManOptionListItemDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['HangManOptionListItem', function(HangManOptionListItem) {
                                return HangManOptionListItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('hangManOptionListItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

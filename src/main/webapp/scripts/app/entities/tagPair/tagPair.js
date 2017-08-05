'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tagPair', {
                parent: 'entity',
                url: '/tagPairs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TagPairs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tagPair/tagPairs.html',
                        controller: 'TagPairController'
                    }
                },
                resolve: {
                }
            })
            .state('tagPair.detail', {
                parent: 'entity',
                url: '/tagPair/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TagPair'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tagPair/tagPair-detail.html',
                        controller: 'TagPairDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'TagPair', function($stateParams, TagPair) {
                        return TagPair.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tagPair.new', {
                parent: 'tagPair',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tagPair/tagPair-dialog.html',
                        controller: 'TagPairDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    tagSlotId: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tagPair', null, { reload: true });
                    }, function() {
                        $state.go('tagPair');
                    })
                }]
            })
            .state('tagPair.edit', {
                parent: 'tagPair',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tagPair/tagPair-dialog.html',
                        controller: 'TagPairDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TagPair', function(TagPair) {
                                return TagPair.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tagPair', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

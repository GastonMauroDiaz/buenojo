'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tagPool', {
                parent: 'entity',
                url: '/tagPools',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TagPools'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tagPool/tagPools.html',
                        controller: 'TagPoolController'
                    }
                },
                resolve: {
                }
            })
            .state('tagPool.detail', {
                parent: 'entity',
                url: '/tagPool/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TagPool'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tagPool/tagPool-detail.html',
                        controller: 'TagPoolDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'TagPool', function($stateParams, TagPool) {
                        return TagPool.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tagPool.new', {
                parent: 'tagPool',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tagPool/tagPool-dialog.html',
                        controller: 'TagPoolDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    similarity: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tagPool', null, { reload: true });
                    }, function() {
                        $state.go('tagPool');
                    })
                }]
            })
            .state('tagPool.edit', {
                parent: 'tagPool',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tagPool/tagPool-dialog.html',
                        controller: 'TagPoolDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TagPool', function(TagPool) {
                                return TagPool.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tagPool', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

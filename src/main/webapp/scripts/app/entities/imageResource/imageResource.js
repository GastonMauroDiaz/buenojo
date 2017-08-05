'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('imageResource', {
                parent: 'entity',
                url: '/imageResources',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ImageResources'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imageResource/imageResources.html',
                        controller: 'ImageResourceController'
                    }
                },
                resolve: {
                }
            })
            .state('imageResource.detail', {
                parent: 'entity',
                url: '/imageResource/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ImageResource'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imageResource/imageResource-detail.html',
                        controller: 'ImageResourceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ImageResource', function($stateParams, ImageResource) {
                        return ImageResource.get({id : $stateParams.id});
                    }]
                }
            })
            .state('imageResource.new', {
                parent: 'imageResource',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imageResource/imageResource-dialog.html',
                        controller: 'ImageResourceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    loResImage: null,
                                    loResImageContentType: null,
                                    hiResImage: null,
                                    hiResImageContentType: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('imageResource', null, { reload: true });
                    }, function() {
                        $state.go('imageResource');
                    })
                }]
            })
            .state('imageResource.edit', {
                parent: 'imageResource',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imageResource/imageResource-dialog.html',
                        controller: 'ImageResourceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ImageResource', function(ImageResource) {
                                return ImageResource.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('imageResource', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationImage', {
                parent: 'entity',
                url: '/photoLocationImages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationImages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationImage/photoLocationImages.html',
                        controller: 'PhotoLocationImageController'
                    }
                },
                resolve: {
                }
            })
            .state('photoLocationImage.detail', {
                parent: 'entity',
                url: '/photoLocationImage/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationImage'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationImage/photoLocationImage-detail.html',
                        controller: 'PhotoLocationImageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PhotoLocationImage', function($stateParams, PhotoLocationImage) {
                        return PhotoLocationImage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photoLocationImage.new', {
                parent: 'photoLocationImage',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationImage/photoLocationImage-dialog.html',
                        controller: 'PhotoLocationImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationImage', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationImage');
                    })
                }]
            })
            .state('photoLocationImage.edit', {
                parent: 'photoLocationImage',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationImage/photoLocationImage-dialog.html',
                        controller: 'PhotoLocationImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PhotoLocationImage', function(PhotoLocationImage) {
                                return PhotoLocationImage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationImage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

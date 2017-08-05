'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationSatelliteImage', {
                parent: 'entity',
                url: '/photoLocationSatelliteImages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationSatelliteImages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationSatelliteImage/photoLocationSatelliteImages.html',
                        controller: 'PhotoLocationSatelliteImageController'
                    }
                },
                resolve: {
                }
            })
            .state('photoLocationSatelliteImage.detail', {
                parent: 'entity',
                url: '/photoLocationSatelliteImage/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationSatelliteImage'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationSatelliteImage/photoLocationSatelliteImage-detail.html',
                        controller: 'PhotoLocationSatelliteImageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PhotoLocationSatelliteImage', function($stateParams, PhotoLocationSatelliteImage) {
                        return PhotoLocationSatelliteImage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photoLocationSatelliteImage.new', {
                parent: 'photoLocationSatelliteImage',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationSatelliteImage/photoLocationSatelliteImage-dialog.html',
                        controller: 'PhotoLocationSatelliteImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationSatelliteImage', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationSatelliteImage');
                    })
                }]
            })
            .state('photoLocationSatelliteImage.edit', {
                parent: 'photoLocationSatelliteImage',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationSatelliteImage/photoLocationSatelliteImage-dialog.html',
                        controller: 'PhotoLocationSatelliteImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PhotoLocationSatelliteImage', function(PhotoLocationSatelliteImage) {
                                return PhotoLocationSatelliteImage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationSatelliteImage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

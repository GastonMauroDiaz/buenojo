'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('satelliteImage', {
                parent: 'entity',
                url: '/satelliteImages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'SatelliteImages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/satelliteImage/satelliteImages.html',
                        controller: 'SatelliteImageController'
                    }
                },
                resolve: {
                }
            })
            .state('satelliteImage.detail', {
                parent: 'entity',
                url: '/satelliteImage/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'SatelliteImage'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/satelliteImage/satelliteImage-detail.html',
                        controller: 'SatelliteImageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SatelliteImage', function($stateParams, SatelliteImage) {
                        return SatelliteImage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('satelliteImage.new', {
                parent: 'satelliteImage',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/satelliteImage/satelliteImage-dialog.html',
                        controller: 'SatelliteImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    meters: null,
                                    lon: null,
                                    lat: null,
                                    resolution: null,
                                    name: null,
                                    image: null,
                                    imageContentType: null,
                                    copyright: null,
                                    imageType: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('satelliteImage', null, { reload: true });
                    }, function() {
                        $state.go('satelliteImage');
                    })
                }]
            })
            .state('satelliteImage.upload', {
                parent: 'satelliteImage',
                url: '/upload',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/satelliteImage/satelliteImage-upload-dialog.html',
                        controller: 'SatelliteImageUploadController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    
                                    imageFile: null,
                                    csvFile: null,
                                    
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('satelliteImage', null, { reload: true });
                    }, function() {
                        $state.go('satelliteImage');
                    })
                }]
            })
            .state('satelliteImage.edit', {
                parent: 'satelliteImage',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/satelliteImage/satelliteImage-dialog.html',
                        controller: 'SatelliteImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SatelliteImage', function(SatelliteImage) {
                                return SatelliteImage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('satelliteImage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

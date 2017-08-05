'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationExtraSatelliteImage', {
                parent: 'entity',
                url: '/photoLocationExtraSatelliteImages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationExtraSatelliteImages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationExtraSatelliteImage/photoLocationExtraSatelliteImages.html',
                        controller: 'PhotoLocationExtraSatelliteImageController'
                    }
                },
                resolve: {
                }
            })
            .state('photoLocationExtraSatelliteImage.detail', {
                parent: 'entity',
                url: '/photoLocationExtraSatelliteImage/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationExtraSatelliteImage'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationExtraSatelliteImage/photoLocationExtraSatelliteImage-detail.html',
                        controller: 'PhotoLocationExtraSatelliteImageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PhotoLocationExtraSatelliteImage', function($stateParams, PhotoLocationExtraSatelliteImage) {
                        return PhotoLocationExtraSatelliteImage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photoLocationExtraSatelliteImage.new', {
                parent: 'photoLocationExtraSatelliteImage',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationExtraSatelliteImage/photoLocationExtraSatelliteImage-dialog.html',
                        controller: 'PhotoLocationExtraSatelliteImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationExtraSatelliteImage', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationExtraSatelliteImage');
                    })
                }]
            })
            .state('photoLocationExtraSatelliteImage.upload', {
                parent: 'photoLocationExtraSatelliteImage',
                url: '/upload',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationExtraSatelliteImage/photoLocationExtraSatelliteImage-upload-dialog.html',
                        controller: 'PhotoLocationExtraSatelliteImageUploadDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationExtraSatelliteImage', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationExtraSatelliteImage');
                    })
                }]
            })
            .state('photoLocationExtraSatelliteImage.edit', {
                parent: 'photoLocationExtraSatelliteImage',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationExtraSatelliteImage/photoLocationExtraSatelliteImage-dialog.html',
                        controller: 'PhotoLocationExtraSatelliteImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PhotoLocationExtraSatelliteImage', function(PhotoLocationExtraSatelliteImage) {
                                return PhotoLocationExtraSatelliteImage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationExtraSatelliteImage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

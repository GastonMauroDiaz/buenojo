'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationSightPair', {
                parent: 'entity',
                url: '/photoLocationSightPairs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationSightPairs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationSightPair/photoLocationSightPairs.html',
                        controller: 'PhotoLocationSightPairController'
                    }
                },
                resolve: {
                }
            })
            .state('photoLocationSightPair.detail', {
                parent: 'entity',
                url: '/photoLocationSightPair/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationSightPair'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationSightPair/photoLocationSightPair-detail.html',
                        controller: 'PhotoLocationSightPairDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PhotoLocationSightPair', function($stateParams, PhotoLocationSightPair) {
                        return PhotoLocationSightPair.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photoLocationSightPair.new', {
                parent: 'photoLocationSightPair',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationSightPair/photoLocationSightPair-dialog.html',
                        controller: 'PhotoLocationSightPairDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    number: null,
                                    satelliteX: null,
                                    satelliteY: null,
                                    satelliteTolerance: null,
                                    terrainX: null,
                                    terrainY: null,
                                    terrainTolerance: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationSightPair', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationSightPair');
                    })
                }]
            })
            .state('photoLocationSightPair.upload', {
                parent: 'photoLocationSightPair',
                url: '/upload',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationSightPair/photoLocationSightPair-upload-dialog.html',
                        controller: 'PhotoLocationSightPairUploadDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    number: null,
                                    satelliteX: null,
                                    satelliteY: null,
                                    satelliteTolerance: null,
                                    terrainX: null,
                                    terrainY: null,
                                    terrainTolerance: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationSightPair', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationSightPair');
                    })
                }]
            })
            .state('photoLocationSightPair.edit', {
                parent: 'photoLocationSightPair',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationSightPair/photoLocationSightPair-dialog.html',
                        controller: 'PhotoLocationSightPairDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PhotoLocationSightPair', function(PhotoLocationSightPair) {
                                return PhotoLocationSightPair.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationSightPair', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

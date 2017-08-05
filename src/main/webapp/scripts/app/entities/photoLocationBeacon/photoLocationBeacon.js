'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationBeacon', {
                parent: 'entity',
                url: '/photoLocationBeacons',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationBeacons'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationBeacon/photoLocationBeacons.html',
                        controller: 'PhotoLocationBeaconController'
                    }
                },
                resolve: {
                }
            })
            .state('photoLocationBeacon.detail', {
                parent: 'entity',
                url: '/photoLocationBeacon/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationBeacon'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationBeacon/photoLocationBeacon-detail.html',
                        controller: 'PhotoLocationBeaconDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PhotoLocationBeacon', function($stateParams, PhotoLocationBeacon) {
                        return PhotoLocationBeacon.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photoLocationBeacon.new', {
                parent: 'photoLocationBeacon',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationBeacon/photoLocationBeacon-dialog.html',
                        controller: 'PhotoLocationBeaconDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    x: null,
                                    y: null,
                                    tolerance: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationBeacon', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationBeacon');
                    })
                }]
            })
            .state('photoLocationBeacon.edit', {
                parent: 'photoLocationBeacon',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationBeacon/photoLocationBeacon-dialog.html',
                        controller: 'PhotoLocationBeaconDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PhotoLocationBeacon', function(PhotoLocationBeacon) {
                                return PhotoLocationBeacon.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationBeacon', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

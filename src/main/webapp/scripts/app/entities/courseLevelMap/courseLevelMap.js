'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('courseLevelMap', {
                parent: 'entity',
                url: '/courseLevelMaps',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CourseLevelMaps'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/courseLevelMap/courseLevelMaps.html',
                        controller: 'CourseLevelMapController'
                    }
                },
                resolve: {
                }
            })
            .state('courseLevelMap.detail', {
                parent: 'entity',
                url: '/courseLevelMap/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CourseLevelMap'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/courseLevelMap/courseLevelMap-detail.html',
                        controller: 'CourseLevelMapDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CourseLevelMap', function($stateParams, CourseLevelMap) {
                        return CourseLevelMap.get({id : $stateParams.id});
                    }]
                }
            })
            .state('courseLevelMap.new', {
                parent: 'courseLevelMap',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/courseLevelMap/courseLevelMap-dialog.html',
                        controller: 'CourseLevelMapDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('courseLevelMap', null, { reload: true });
                    }, function() {
                        $state.go('courseLevelMap');
                    })
                }]
            })
            .state('courseLevelMap.edit', {
                parent: 'courseLevelMap',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/courseLevelMap/courseLevelMap-dialog.html',
                        controller: 'CourseLevelMapDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CourseLevelMap', function(CourseLevelMap) {
                                return CourseLevelMap.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('courseLevelMap', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('courseLevelMap.delete', {
                parent: 'courseLevelMap',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/courseLevelMap/courseLevelMap-delete-dialog.html',
                        controller: 'CourseLevelMapDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CourseLevelMap', function(CourseLevelMap) {
                                return CourseLevelMap.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('courseLevelMap', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

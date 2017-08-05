'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('level', {
                parent: 'entity',
                url: '/levels',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Levels'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/level/levels.html',
                        controller: 'LevelController'
                    }
                },
                resolve: {
                }
            })
            .state('level.detail', {
                parent: 'entity',
                url: '/level/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Level'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/level/level-detail.html',
                        controller: 'LevelDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Level', function($stateParams, Level) {
                        return Level.get({id : $stateParams.id});
                    }]
                }
            })
            .state('level.new', {
                parent: 'level',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/level/level-dialog.html',
                        controller: 'LevelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    levelOrder: null,
                                    name: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('level', null, { reload: true });
                    }, function() {
                        $state.go('level');
                    })
                }]
            })
            .state('level.edit', {
                parent: 'level',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/level/level-dialog.html',
                        controller: 'LevelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Level', function(Level) {
                                return Level.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('level', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('level.delete', {
                parent: 'level',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/level/level-delete-dialog.html',
                        controller: 'LevelDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Level', function(Level) {
                                return Level.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('level', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('currentSession', {
                parent: 'entity',
                url: '/currentSessions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CurrentSessions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/currentSession/currentSessions.html',
                        controller: 'CurrentSessionController'
                    }
                },
                resolve: {
                }
            })
            .state('currentSession.detail', {
                parent: 'entity',
                url: '/currentSession/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CurrentSession'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/currentSession/currentSession-detail.html',
                        controller: 'CurrentSessionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CurrentSession', function($stateParams, CurrentSession) {
                        return CurrentSession.get({id : $stateParams.id});
                    }]
                }
            })
            .state('currentSession.new', {
                parent: 'currentSession',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/currentSession/currentSession-dialog.html',
                        controller: 'CurrentSessionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('currentSession', null, { reload: true });
                    }, function() {
                        $state.go('currentSession');
                    })
                }]
            })
            .state('currentSession.edit', {
                parent: 'currentSession',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/currentSession/currentSession-dialog.html',
                        controller: 'CurrentSessionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CurrentSession', function(CurrentSession) {
                                return CurrentSession.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('currentSession', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('currentSession.delete', {
                parent: 'currentSession',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/currentSession/currentSession-delete-dialog.html',
                        controller: 'CurrentSessionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CurrentSession', function(CurrentSession) {
                                return CurrentSession.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('currentSession', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

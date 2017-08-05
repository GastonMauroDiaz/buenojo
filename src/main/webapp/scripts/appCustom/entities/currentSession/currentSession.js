'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('currentSessionCustom', {
                parent: 'entity',
                url: '/currentSessionsCustom',
                data: {
                    authorities: ['ROLE_USER','ROLE_DEMO_USER', 'ROLE_USER', 'ROLE_COURSE_STUDENT'],
                    pageTitle: 'CurrentSessions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/currentSession/currentSessions.html',
                        controller: 'CurrentSessionCustomController'
                    }
                },
                resolve: {
                }
            })
            .state('currentSessionCustom.detail', {
                parent: 'entity',
                url: '/currentSession/{id}',
                data: {
                    authorities: ['ROLE_USER','ROLE_DEMO_USER', 'ROLE_USER', 'ROLE_COURSE_STUDENT'],
                    pageTitle: 'CurrentSession'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/currentSession/currentSession-detail.html',
                        controller: 'CurrentSessionCustomDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CurrentSession', function($stateParams, CurrentSession) {
                        return CurrentSession.get({id : $stateParams.id});
                    }]
                }
            })
            .state('currentSessionCustom.new', {
                parent: 'currentSessionCustom',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER','ROLE_DEMO_USER', 'ROLE_USER', 'ROLE_COURSE_STUDENT'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/appCustom/entities/currentSession/currentSession-dialog.html',
                        controller: 'CurrentSessionCustomDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('currentSessionCustom', null, { reload: true });
                    }, function() {
                        $state.go('currentSessionCustom');
                    })
                }]
            })
            .state('currentSessionCustom.edit', {
                parent: 'currentSessionCustom',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_DEMO_USER', 'ROLE_USER', 'ROLE_COURSE_STUDENT'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/appCustom/entities/currentSession/currentSession-dialog.html',
                        controller: 'CurrentSessionCustomDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CurrentSession', function(CurrentSession) {
                                return CurrentSession.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('currentSessionCustom', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('currentSessionCustom.delete', {
                parent: 'currentSessionCustom',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER','ROLE_DEMO_USER', 'ROLE_USER', 'ROLE_COURSE_STUDENT'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/appCustom/entities/currentSession/currentSession-delete-dialog.html',
                        controller: 'CurrentSessionCustomDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CurrentSession', function(CurrentSession) {
                                return CurrentSession.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('currentSessionCustom', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

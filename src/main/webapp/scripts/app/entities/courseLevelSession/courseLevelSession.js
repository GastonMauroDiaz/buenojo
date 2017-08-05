'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('courseLevelSession', {
                parent: 'entity',
                url: '/courseLevelSessions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CourseLevelSessions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/courseLevelSession/courseLevelSessions.html',
                        controller: 'CourseLevelSessionController'
                    }
                },
                resolve: {
                }
            })
            .state('courseLevelSession.detail', {
                parent: 'entity',
                url: '/courseLevelSession/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CourseLevelSession'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/courseLevelSession/courseLevelSession-detail.html',
                        controller: 'CourseLevelSessionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CourseLevelSession', function($stateParams, CourseLevelSession) {
                        return CourseLevelSession.get({id : $stateParams.id});
                    }]
                }
            })
            .state('courseLevelSession.new', {
                parent: 'courseLevelSession',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/courseLevelSession/courseLevelSession-dialog.html',
                        controller: 'CourseLevelSessionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    percentage: null,
                                    experiencePoints: null,
                                    exerciseCompletedCount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('courseLevelSession', null, { reload: true });
                    }, function() {
                        $state.go('courseLevelSession');
                    })
                }]
            })
            .state('courseLevelSession.edit', {
                parent: 'courseLevelSession',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/courseLevelSession/courseLevelSession-dialog.html',
                        controller: 'CourseLevelSessionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CourseLevelSession', function(CourseLevelSession) {
                                return CourseLevelSession.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('courseLevelSession', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('courseLevelSession.delete', {
                parent: 'courseLevelSession',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/courseLevelSession/courseLevelSession-delete-dialog.html',
                        controller: 'CourseLevelSessionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CourseLevelSession', function(CourseLevelSession) {
                                return CourseLevelSession.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('courseLevelSession', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

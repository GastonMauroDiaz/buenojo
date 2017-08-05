'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('activityTrace', {
                parent: 'entity',
                url: '/activityTraces',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ActivityTraces'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/activityTrace/activityTraces.html',
                        controller: 'ActivityTraceController'
                    }
                },
                resolve: {
                }
            })
            .state('activityTrace.detail', {
                parent: 'entity',
                url: '/activityTrace/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ActivityTrace'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/activityTrace/activityTrace-detail.html',
                        controller: 'ActivityTraceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ActivityTrace', function($stateParams, ActivityTrace) {
                        return ActivityTrace.get({id : $stateParams.id});
                    }]
                }
            })
            .state('activityTrace.new', {
                parent: 'activityTrace',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/activityTrace/activityTrace-dialog.html',
                        controller: 'ActivityTraceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    startDate: null,
                                    endDate: null,
                                    score: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('activityTrace', null, { reload: true });
                    }, function() {
                        $state.go('activityTrace');
                    })
                }]
            })
            .state('activityTrace.edit', {
                parent: 'activityTrace',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/activityTrace/activityTrace-dialog.html',
                        controller: 'ActivityTraceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ActivityTrace', function(ActivityTrace) {
                                return ActivityTrace.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('activityTrace', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

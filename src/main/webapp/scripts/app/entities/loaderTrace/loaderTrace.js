'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('loaderTrace', {
                parent: 'entity',
                url: '/loaderTraces',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'LoaderTraces'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/loaderTrace/loaderTraces.html',
                        controller: 'LoaderTraceController'
                    }
                },
                resolve: {
                }
            })
            .state('loaderTrace.detail', {
                parent: 'entity',
                url: '/loaderTrace/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'LoaderTrace'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/loaderTrace/loaderTrace-detail.html',
                        controller: 'LoaderTraceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'LoaderTrace', function($stateParams, LoaderTrace) {
                        return LoaderTrace.get({id : $stateParams.id});
                    }]
                }
            })
            .state('loaderTrace.new', {
                parent: 'loaderTrace',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/loaderTrace/loaderTrace-dialog.html',
                        controller: 'LoaderTraceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    loaderResult: null,
                                    author: null,
                                    loaderType: null,
                                    date: null,
                                    resultLog: null,
                                    datasetName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('loaderTrace', null, { reload: true });
                    }, function() {
                        $state.go('loaderTrace');
                    })
                }]
            })
            .state('loaderTrace.edit', {
                parent: 'loaderTrace',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/loaderTrace/loaderTrace-dialog.html',
                        controller: 'LoaderTraceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LoaderTrace', function(LoaderTrace) {
                                return LoaderTrace.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('loaderTrace', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

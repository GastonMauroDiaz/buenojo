'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tagCircle', {
                parent: 'entity',
                url: '/tagCircles',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TagCircles'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tagCircle/tagCircles.html',
                        controller: 'TagCircleController'
                    }
                },
                resolve: {
                }
            })
            .state('tagCircle.detail', {
                parent: 'entity',
                url: '/tagCircle/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TagCircle'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tagCircle/tagCircle-detail.html',
                        controller: 'TagCircleDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'TagCircle', function($stateParams, TagCircle) {
                        return TagCircle.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tagCircle.new', {
                parent: 'tagCircle',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tagCircle/tagCircle-dialog.html',
                        controller: 'TagCircleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    number: null,
                                    y: null,
                                    x: null,
                                    radioPx: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tagCircle', null, { reload: true });
                    }, function() {
                        $state.go('tagCircle');
                    })
                }]
            })
            .state('tagCircle.upload', {
                parent: 'tagCircle',
                url: '/upload',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tagCircle/tagCircle-upload-dialog.html',
                        controller: 'TagCircleUploadDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    number: null,
                                    y: null,
                                    x: null,
                                    radioPx: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tagCircle', null, { reload: true });
                    }, function() {
                        $state.go('tagCircle');
                    })
                }]
            })
            .state('tagCircle.edit', {
                parent: 'tagCircle',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tagCircle/tagCircle-dialog.html',
                        controller: 'TagCircleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TagCircle', function(TagCircle) {
                                return TagCircle.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tagCircle', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

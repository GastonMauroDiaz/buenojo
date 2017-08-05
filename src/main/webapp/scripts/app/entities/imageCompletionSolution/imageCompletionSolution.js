'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('imageCompletionSolution', {
                parent: 'entity',
                url: '/imageCompletionSolutions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ImageCompletionSolutions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imageCompletionSolution/imageCompletionSolutions.html',
                        controller: 'ImageCompletionSolutionController'
                    }
                },
                resolve: {
                }
            })
            .state('imageCompletionSolution.detail', {
                parent: 'entity',
                url: '/imageCompletionSolution/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ImageCompletionSolution'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imageCompletionSolution/imageCompletionSolution-detail.html',
                        controller: 'ImageCompletionSolutionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ImageCompletionSolution', function($stateParams, ImageCompletionSolution) {
                        return ImageCompletionSolution.get({id : $stateParams.id});
                    }]
                }
            })
            .state('imageCompletionSolution.new', {
                parent: 'imageCompletionSolution',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imageCompletionSolution/imageCompletionSolution-dialog.html',
                        controller: 'ImageCompletionSolutionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('imageCompletionSolution', null, { reload: true });
                    }, function() {
                        $state.go('imageCompletionSolution');
                    })
                }]
            })
            .state('imageCompletionSolution.upload', {
                parent: 'imageCompletionSolution',
                url: '/upload',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imageCompletionSolution/imageCompletionSolution-upload-dialog.html',
                        controller: 'ImageCompletionSolutionUploadDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('imageCompletionSolution', null, { reload: true });
                    }, function() {
                        $state.go('imageCompletionSolution');
                    })
                }]
            })
            .state('imageCompletionSolution.edit', {
                parent: 'imageCompletionSolution',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imageCompletionSolution/imageCompletionSolution-dialog.html',
                        controller: 'ImageCompletionSolutionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ImageCompletionSolution', function(ImageCompletionSolution) {
                                return ImageCompletionSolution.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('imageCompletionSolution', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

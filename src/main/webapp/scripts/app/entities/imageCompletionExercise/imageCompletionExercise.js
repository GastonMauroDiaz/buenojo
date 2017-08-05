'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('imageCompletionExercise', {
                parent: 'entity',
                url: '/imageCompletionExercises',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ImageCompletionExercises'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imageCompletionExercise/imageCompletionExercises.html',
                        controller: 'ImageCompletionExerciseController'
                    }
                },
                resolve: {
                }
            })
            .state('imageCompletionExercise.detail', {
                parent: 'entity',
                url: '/imageCompletionExercise/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ImageCompletionExercise'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imageCompletionExercise/imageCompletionExercise-detail.html',
                        controller: 'ImageCompletionExerciseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ImageCompletionExercise', function($stateParams, ImageCompletionExercise) {
                        return ImageCompletionExercise.get({id : $stateParams.id});
                    }]
                }
            })
            .state('imageCompletionExercise.new', {
                parent: 'imageCompletionExercise',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imageCompletionExercise/imageCompletionExercise-dialog.html',
                        controller: 'ImageCompletionExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('imageCompletionExercise', null, { reload: true });
                    }, function() {
                        $state.go('imageCompletionExercise');
                    })
                }]
            })
            .state('imageCompletionExercise.edit', {
                parent: 'imageCompletionExercise',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imageCompletionExercise/imageCompletionExercise-dialog.html',
                        controller: 'ImageCompletionExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ImageCompletionExercise', function(ImageCompletionExercise) {
                                return ImageCompletionExercise.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('imageCompletionExercise', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

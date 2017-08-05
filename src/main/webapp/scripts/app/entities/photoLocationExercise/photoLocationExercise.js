'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationExercise', {
                parent: 'entity',
                url: '/photoLocationExercises',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationExercises'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationExercise/photoLocationExercises.html',
                        controller: 'PhotoLocationExerciseController'
                    }
                },
                resolve: {
                }
            })
            .state('photoLocationExercise.detail', {
                parent: 'entity',
                url: '/photoLocationExercise/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationExercise'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationExercise/photoLocationExercise-detail.html',
                        controller: 'PhotoLocationExerciseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PhotoLocationExercise', function($stateParams, PhotoLocationExercise) {
                        return PhotoLocationExercise.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photoLocationExercise.new', {
                parent: 'photoLocationExercise',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationExercise/photoLocationExercise-dialog.html',
                        controller: 'PhotoLocationExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    totalScore: null,
                                    totalTimeInSeconds: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationExercise', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationExercise');
                    })
                }]
            })
            .state('photoLocationExercise.edit', {
                parent: 'photoLocationExercise',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationExercise/photoLocationExercise-dialog.html',
                        controller: 'PhotoLocationExerciseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PhotoLocationExercise', function(PhotoLocationExercise) {
                                return PhotoLocationExercise.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationExercise', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

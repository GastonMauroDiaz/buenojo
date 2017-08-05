'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceExerciseContainer', {
                parent: 'entity',
                url: '/multipleChoiceExerciseContainers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceExerciseContainers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceExerciseContainer/multipleChoiceExerciseContainers.html',
                        controller: 'MultipleChoiceExerciseContainerController'
                    }
                },
                resolve: {
                }
            })
            .state('multipleChoiceExerciseContainer.detail', {
                parent: 'entity',
                url: '/multipleChoiceExerciseContainer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceExerciseContainer'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceExerciseContainer/multipleChoiceExerciseContainer-detail.html',
                        controller: 'MultipleChoiceExerciseContainerDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MultipleChoiceExerciseContainer', function($stateParams, MultipleChoiceExerciseContainer) {
                        return MultipleChoiceExerciseContainer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('multipleChoiceExerciseContainer.new', {
                parent: 'multipleChoiceExerciseContainer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceExerciseContainer/multipleChoiceExerciseContainer-dialog.html',
                        controller: 'MultipleChoiceExerciseContainerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceExerciseContainer', null, { reload: true });
                    }, function() {
                        $state.go('multipleChoiceExerciseContainer');
                    })
                }]
            })
            .state('multipleChoiceExerciseContainer.edit', {
                parent: 'multipleChoiceExerciseContainer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceExerciseContainer/multipleChoiceExerciseContainer-dialog.html',
                        controller: 'MultipleChoiceExerciseContainerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MultipleChoiceExerciseContainer', function(MultipleChoiceExerciseContainer) {
                                return MultipleChoiceExerciseContainer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceExerciseContainer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('multipleChoiceExerciseContainer.delete', {
                parent: 'multipleChoiceExerciseContainer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceExerciseContainer/multipleChoiceExerciseContainer-delete-dialog.html',
                        controller: 'MultipleChoiceExerciseContainerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MultipleChoiceExerciseContainer', function(MultipleChoiceExerciseContainer) {
                                return MultipleChoiceExerciseContainer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceExerciseContainer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

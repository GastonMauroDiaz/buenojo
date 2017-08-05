'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceQuestionImageResource', {
                parent: 'entity',
                url: '/multipleChoiceQuestionImageResources',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceQuestionImageResources'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestionImageResource/multipleChoiceQuestionImageResources.html',
                        controller: 'MultipleChoiceQuestionImageResourceController'
                    }
                },
                resolve: {
                }
            })
            .state('multipleChoiceQuestionImageResource.detail', {
                parent: 'entity',
                url: '/multipleChoiceQuestionImageResource/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceQuestionImageResource'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestionImageResource/multipleChoiceQuestionImageResource-detail.html',
                        controller: 'MultipleChoiceQuestionImageResourceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MultipleChoiceQuestionImageResource', function($stateParams, MultipleChoiceQuestionImageResource) {
                        return MultipleChoiceQuestionImageResource.get({id : $stateParams.id});
                    }]
                }
            })
            .state('multipleChoiceQuestionImageResource.new', {
                parent: 'multipleChoiceQuestionImageResource',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestionImageResource/multipleChoiceQuestionImageResource-dialog.html',
                        controller: 'MultipleChoiceQuestionImageResourceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceQuestionImageResource', null, { reload: true });
                    }, function() {
                        $state.go('multipleChoiceQuestionImageResource');
                    })
                }]
            })
            .state('multipleChoiceQuestionImageResource.edit', {
                parent: 'multipleChoiceQuestionImageResource',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestionImageResource/multipleChoiceQuestionImageResource-dialog.html',
                        controller: 'MultipleChoiceQuestionImageResourceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MultipleChoiceQuestionImageResource', function(MultipleChoiceQuestionImageResource) {
                                return MultipleChoiceQuestionImageResource.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceQuestionImageResource', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('multipleChoiceQuestionImageResource.delete', {
                parent: 'multipleChoiceQuestionImageResource',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestionImageResource/multipleChoiceQuestionImageResource-delete-dialog.html',
                        controller: 'MultipleChoiceQuestionImageResourceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MultipleChoiceQuestionImageResource', function(MultipleChoiceQuestionImageResource) {
                                return MultipleChoiceQuestionImageResource.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceQuestionImageResource', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

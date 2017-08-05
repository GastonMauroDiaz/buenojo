'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceAnswer', {
                parent: 'entity',
                url: '/multipleChoiceAnswers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceAnswers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceAnswer/multipleChoiceAnswers.html',
                        controller: 'MultipleChoiceAnswerController'
                    }
                },
                resolve: {
                }
            })
            .state('multipleChoiceAnswer.detail', {
                parent: 'entity',
                url: '/multipleChoiceAnswer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceAnswer'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceAnswer/multipleChoiceAnswer-detail.html',
                        controller: 'MultipleChoiceAnswerDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MultipleChoiceAnswer', function($stateParams, MultipleChoiceAnswer) {
                        return MultipleChoiceAnswer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('multipleChoiceAnswer.new', {
                parent: 'multipleChoiceAnswer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceAnswer/multipleChoiceAnswer-dialog.html',
                        controller: 'MultipleChoiceAnswerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    answer: null,
                                    isRight: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceAnswer', null, { reload: true });
                    }, function() {
                        $state.go('multipleChoiceAnswer');
                    })
                }]
            })
            .state('multipleChoiceAnswer.edit', {
                parent: 'multipleChoiceAnswer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceAnswer/multipleChoiceAnswer-dialog.html',
                        controller: 'MultipleChoiceAnswerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MultipleChoiceAnswer', function(MultipleChoiceAnswer) {
                                return MultipleChoiceAnswer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceAnswer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('multipleChoiceAnswer.delete', {
                parent: 'multipleChoiceAnswer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceAnswer/multipleChoiceAnswer-delete-dialog.html',
                        controller: 'MultipleChoiceAnswerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MultipleChoiceAnswer', function(MultipleChoiceAnswer) {
                                return MultipleChoiceAnswer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceAnswer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

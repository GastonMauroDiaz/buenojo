'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceQuestion', {
                parent: 'entity',
                url: '/multipleChoiceQuestions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceQuestions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestion/multipleChoiceQuestions.html',
                        controller: 'MultipleChoiceQuestionController'
                    }
                },
                resolve: {
                }
            })
            .state('multipleChoiceQuestion.detail', {
                parent: 'entity',
                url: '/multipleChoiceQuestion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceQuestion'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestion/multipleChoiceQuestion-detail.html',
                        controller: 'MultipleChoiceQuestionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MultipleChoiceQuestion', function($stateParams, MultipleChoiceQuestion) {
                        return MultipleChoiceQuestion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('multipleChoiceQuestion.new', {
                parent: 'multipleChoiceQuestion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestion/multipleChoiceQuestion-dialog.html',
                        controller: 'MultipleChoiceQuestionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    question: null,
                                    interactionType: null,
                                    exerciseId: null,
                                    source: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceQuestion', null, { reload: true });
                    }, function() {
                        $state.go('multipleChoiceQuestion');
                    })
                }]
            })
            .state('multipleChoiceQuestion.edit', {
                parent: 'multipleChoiceQuestion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestion/multipleChoiceQuestion-dialog.html',
                        controller: 'MultipleChoiceQuestionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MultipleChoiceQuestion', function(MultipleChoiceQuestion) {
                                return MultipleChoiceQuestion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceQuestion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('multipleChoiceQuestion.delete', {
                parent: 'multipleChoiceQuestion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceQuestion/multipleChoiceQuestion-delete-dialog.html',
                        controller: 'MultipleChoiceQuestionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MultipleChoiceQuestion', function(MultipleChoiceQuestion) {
                                return MultipleChoiceQuestion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceQuestion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

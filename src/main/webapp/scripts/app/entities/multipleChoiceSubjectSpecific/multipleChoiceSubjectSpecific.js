'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceSubjectSpecific', {
                parent: 'entity',
                url: '/multipleChoiceSubjectSpecifics',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceSubjectSpecifics'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceSubjectSpecific/multipleChoiceSubjectSpecifics.html',
                        controller: 'MultipleChoiceSubjectSpecificController'
                    }
                },
                resolve: {
                }
            })
            .state('multipleChoiceSubjectSpecific.detail', {
                parent: 'entity',
                url: '/multipleChoiceSubjectSpecific/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceSubjectSpecific'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceSubjectSpecific/multipleChoiceSubjectSpecific-detail.html',
                        controller: 'MultipleChoiceSubjectSpecificDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MultipleChoiceSubjectSpecific', function($stateParams, MultipleChoiceSubjectSpecific) {
                        return MultipleChoiceSubjectSpecific.get({id : $stateParams.id});
                    }]
                }
            })
            .state('multipleChoiceSubjectSpecific.new', {
                parent: 'multipleChoiceSubjectSpecific',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceSubjectSpecific/multipleChoiceSubjectSpecific-dialog.html',
                        controller: 'MultipleChoiceSubjectSpecificDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    text: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceSubjectSpecific', null, { reload: true });
                    }, function() {
                        $state.go('multipleChoiceSubjectSpecific');
                    })
                }]
            })
            .state('multipleChoiceSubjectSpecific.edit', {
                parent: 'multipleChoiceSubjectSpecific',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceSubjectSpecific/multipleChoiceSubjectSpecific-dialog.html',
                        controller: 'MultipleChoiceSubjectSpecificDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MultipleChoiceSubjectSpecific', function(MultipleChoiceSubjectSpecific) {
                                return MultipleChoiceSubjectSpecific.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceSubjectSpecific', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('multipleChoiceSubjectSpecific.delete', {
                parent: 'multipleChoiceSubjectSpecific',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceSubjectSpecific/multipleChoiceSubjectSpecific-delete-dialog.html',
                        controller: 'MultipleChoiceSubjectSpecificDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MultipleChoiceSubjectSpecific', function(MultipleChoiceSubjectSpecific) {
                                return MultipleChoiceSubjectSpecific.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceSubjectSpecific', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

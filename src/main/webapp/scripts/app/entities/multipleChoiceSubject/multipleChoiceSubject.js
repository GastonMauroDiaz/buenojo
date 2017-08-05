'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceSubject', {
                parent: 'entity',
                url: '/multipleChoiceSubjects',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceSubjects'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceSubject/multipleChoiceSubjects.html',
                        controller: 'MultipleChoiceSubjectController'
                    }
                },
                resolve: {
                }
            })
            .state('multipleChoiceSubject.detail', {
                parent: 'entity',
                url: '/multipleChoiceSubject/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MultipleChoiceSubject'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/multipleChoiceSubject/multipleChoiceSubject-detail.html',
                        controller: 'MultipleChoiceSubjectDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MultipleChoiceSubject', function($stateParams, MultipleChoiceSubject) {
                        return MultipleChoiceSubject.get({id : $stateParams.id});
                    }]
                }
            })
            .state('multipleChoiceSubject.new', {
                parent: 'multipleChoiceSubject',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceSubject/multipleChoiceSubject-dialog.html',
                        controller: 'MultipleChoiceSubjectDialogController',
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
                        $state.go('multipleChoiceSubject', null, { reload: true });
                    }, function() {
                        $state.go('multipleChoiceSubject');
                    })
                }]
            })
            .state('multipleChoiceSubject.edit', {
                parent: 'multipleChoiceSubject',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceSubject/multipleChoiceSubject-dialog.html',
                        controller: 'MultipleChoiceSubjectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MultipleChoiceSubject', function(MultipleChoiceSubject) {
                                return MultipleChoiceSubject.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceSubject', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('multipleChoiceSubject.delete', {
                parent: 'multipleChoiceSubject',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/multipleChoiceSubject/multipleChoiceSubject-delete-dialog.html',
                        controller: 'MultipleChoiceSubjectDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MultipleChoiceSubject', function(MultipleChoiceSubject) {
                                return MultipleChoiceSubject.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('multipleChoiceSubject', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

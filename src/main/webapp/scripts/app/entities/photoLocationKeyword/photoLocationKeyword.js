'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationKeyword', {
                parent: 'entity',
                url: '/photoLocationKeywords',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationKeywords'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationKeyword/photoLocationKeywords.html',
                        controller: 'PhotoLocationKeywordController'
                    }
                },
                resolve: {
                }
            })
            .state('photoLocationKeyword.detail', {
                parent: 'entity',
                url: '/photoLocationKeyword/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationKeyword'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationKeyword/photoLocationKeyword-detail.html',
                        controller: 'PhotoLocationKeywordDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PhotoLocationKeyword', function($stateParams, PhotoLocationKeyword) {
                        return PhotoLocationKeyword.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photoLocationKeyword.new', {
                parent: 'photoLocationKeyword',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationKeyword/photoLocationKeyword-dialog.html',
                        controller: 'PhotoLocationKeywordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationKeyword', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationKeyword');
                    })
                }]
            })
            .state('photoLocationKeyword.edit', {
                parent: 'photoLocationKeyword',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationKeyword/photoLocationKeyword-dialog.html',
                        controller: 'PhotoLocationKeywordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PhotoLocationKeyword', function(PhotoLocationKeyword) {
                                return PhotoLocationKeyword.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationKeyword', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

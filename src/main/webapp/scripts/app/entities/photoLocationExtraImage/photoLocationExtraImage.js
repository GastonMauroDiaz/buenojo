'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photoLocationExtraImage', {
                parent: 'entity',
                url: '/photoLocationExtraImages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationExtraImages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationExtraImage/photoLocationExtraImages.html',
                        controller: 'PhotoLocationExtraImageController'
                    }
                },
                resolve: {
                }
            })
            .state('photoLocationExtraImage.detail', {
                parent: 'entity',
                url: '/photoLocationExtraImage/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PhotoLocationExtraImage'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photoLocationExtraImage/photoLocationExtraImage-detail.html',
                        controller: 'PhotoLocationExtraImageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PhotoLocationExtraImage', function($stateParams, PhotoLocationExtraImage) {
                        return PhotoLocationExtraImage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photoLocationExtraImage.new', {
                parent: 'photoLocationExtraImage',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationExtraImage/photoLocationExtraImage-dialog.html',
                        controller: 'PhotoLocationExtraImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationExtraImage', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationExtraImage');
                    })
                }]
            })
            .state('photoLocationExtraImage.upload', {
                parent: 'photoLocationExtraImage',
                url: '/upload',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationExtraImage/photoLocationExtraImage-upload-dialog.html',
                        controller: 'PhotoLocationExtraImageUploadDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationExtraImage', null, { reload: true });
                    }, function() {
                        $state.go('photoLocationExtraImage');
                    })
                }]
            })
            .state('photoLocationExtraImage.edit', {
                parent: 'photoLocationExtraImage',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/photoLocationExtraImage/photoLocationExtraImage-dialog.html',
                        controller: 'PhotoLocationExtraImageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PhotoLocationExtraImage', function(PhotoLocationExtraImage) {
                                return PhotoLocationExtraImage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photoLocationExtraImage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

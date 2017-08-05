'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('exerciseTip', {
                parent: 'entity',
                url: '/exerciseTips',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ExerciseTips'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/exerciseTip/exerciseTips.html',
                        controller: 'ExerciseTipController'
                    }
                },
                resolve: {
                }
            })
            .state('exerciseTip.detail', {
                parent: 'entity',
                url: '/exerciseTip/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ExerciseTip'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/exerciseTip/exerciseTip-detail.html',
                        controller: 'ExerciseTipDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ExerciseTip', function($stateParams, ExerciseTip) {
                        return ExerciseTip.get({id : $stateParams.id});
                    }]
                }
            })
            .state('exerciseTip.new', {
                parent: 'exerciseTip',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/exerciseTip/exerciseTip-dialog.html',
                        controller: 'ExerciseTipDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    hint: null,
                                    tipDetail: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('exerciseTip', null, { reload: true });
                    }, function() {
                        $state.go('exerciseTip');
                    })
                }]
            })
            .state('exerciseTip.upload', {
                parent: 'exerciseTip',
                url: '/upload',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/exerciseTip/exerciseTip-upload-dialog.html',
                        controller: 'ExerciseTipUploadDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                   
                                    tipDetail: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('exerciseTip', null, { reload: true });
                    }, function() {
                        $state.go('exerciseTip');
                    })
                }]
            })
            .state('exerciseTip.edit', {
                parent: 'exerciseTip',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/exerciseTip/exerciseTip-dialog.html',
                        controller: 'ExerciseTipDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ExerciseTip', function(ExerciseTip) {
                                return ExerciseTip.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('exerciseTip', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

'use strict';

angular.module('buenOjoApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('myProfile', {
                parent: 'entity',
                url: '/myProfile',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_COURSE_STUDENT', 'ROLE_COURSE_ADMIN', "ROLE_ADMIN"],
                },
                views: {
                    'content@': {
                      templateUrl: 'scripts/appCustom/entities/userProfile/userProfile-dialog.html',
                      controller: 'UserProfileDialogCustomController'
                    }
                },
                resolve: {
                  
                }

            })
            .state('userProfile', {
                parent: 'entity',
                url: '/userProfiles',
                data: {
                    authorities: ['ROLE_USER','ROLE_COURSE_ADMIN'],
                    pageTitle: 'UserProfiles'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userProfile/userProfiles.html',
                        controller: 'UserProfileController'
                    }
                },
                resolve: {}
            })
            .state('userProfile.detail', {
                parent: 'entity',
                url: '/userProfile/{id}',
                data: {
                    authorities: ['ROLE_USER','ROLE_COURSE_ADMIN'],
                    pageTitle: 'UserProfile'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userProfile/userProfile-detail.html',
                        controller: 'UserProfileDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserProfile', function($stateParams, UserProfile) {
                        return UserProfile.get({
                            id: $stateParams.id
                        });
                    }]
                }
            })
            .state('userProfile.new', {
                parent: 'userProfile',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userProfile/userProfile-dialog.html',
                        controller: 'UserProfileDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function() {
                                return {
                                    phones: null,
                                    address: null,
                                    picture: null,
                                    pictureContentType: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userProfile', null, {
                            reload: true
                        });
                    }, function() {
                        $state.go('userProfile');
                    })
                }]
            })
            .state('userProfile.edit', {
                parent: 'userProfile',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userProfile/userProfile-dialog.html',
                        controller: 'UserProfileDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserProfile', function(UserProfile) {
                                return UserProfile.get({
                                    id: $stateParams.id
                                });
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userProfile', null, {
                            reload: true
                        });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

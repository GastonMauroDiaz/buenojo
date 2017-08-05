'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('enrollment', {
                parent: 'entity',
                url: '/enrollments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Enrollments'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/enrollment/enrollments.html',
                        controller: 'EnrollmentController'
                    }
                },
                resolve: {
                }
            })
            .state('enrollment.detail', {
                parent: 'entity',
                url: '/enrollment/{id}',
                data: {
                    authorities: ['ROLE_USER','ROLE_COURSE_ADMIN'],
                    pageTitle: 'Enrollment'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/enrollment/enrollment-detail.html',
                        controller: 'EnrollmentDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Enrollment', function($stateParams, Enrollment) {
                        return Enrollment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('enrollment.new', {
                parent: 'enrollment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/enrollment/enrollment-dialog.html',
                        controller: 'EnrollmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('enrollment', null, { reload: true });
                    }, function() {
                        $state.go('enrollment');
                    })
                }]
            })
            .state('enrollment.edit', {
                parent: 'enrollment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/enrollment/enrollment-dialog.html',
                        controller: 'EnrollmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Enrollment', function(Enrollment) {
                                return Enrollment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('enrollment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('enrollment.delete', {
                parent: 'enrollment',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/enrollment/enrollment-delete-dialog.html',
                        controller: 'EnrollmentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Enrollment', function(Enrollment) {
                                return Enrollment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('enrollment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

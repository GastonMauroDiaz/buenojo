'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('course', {
                parent: 'entity',
                url: '/courses',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Courses'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/course/courses.html',
                        controller: 'CourseController'
                    }
                },
                resolve: {
                }
            })
            .state('course.detail', {
                parent: 'entity',
                url: '/course/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Course'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/course/course-detail.html',
                        controller: 'CourseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Course', function($stateParams, Course) {
                        return Course.get({id : $stateParams.id});
                    }]
                }
            })
            .state('course.new', {
                parent: 'course',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/course/course-dialog.html',
                        controller: 'CourseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    startDate: null,
                                    endDate: null,
                                    courseOpen: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('course', null, { reload: true });
                    }, function() {
                        $state.go('course');
                    })
                }]
            })
            .state('course.edit', {
                parent: 'course',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/course/course-dialog.html',
                        controller: 'CourseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Course', function(Course) {
                                return Course.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('course', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

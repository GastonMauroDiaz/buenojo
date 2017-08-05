'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('coursePanel', {
                parent: 'entity',
                url: '/coursePanel/{id}',
                data: {
                    authorities: ['ROLE_COURSE_ADMIN'],
                    pageTitle: 'Panel de Curso'
                },
                params: {
                  course: null
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/coursePanel/coursePanel.html',
                        controller: 'CoursePanelController'
                    }
                },
                resolve: {
                }
            })
            .state('coursePanel.enrollment', {
                parent: 'coursePanel',
                url: '/enrollment/{userId}',
                data: {
                    authorities: ['ROLE_COURSE_ADMIN'],
                },
                params: {
                  courseId: null,
                  userId: null
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/appCustom/entities/coursePanel/enrollmentStatus/enrollmentStatusTab.html',
                        controller: 'EnrollmentStatusTabController',
                        size: 'lg',
                        resolve: {
                            entity: ['Enrollment', function(Enrollment) {
                                return Enrollment.courseAndUser({userId : $stateParams.userId, courseId: $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('coursePanel');
                    }, function() {
                        $state.go('^');
                    })
                }]
            });;




    });

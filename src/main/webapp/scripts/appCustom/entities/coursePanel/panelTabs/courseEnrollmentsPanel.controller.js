'use strict';

angular.module('buenOjoApp')
    .controller('CourseEnrollmentsPanelController', function($scope, $stateParams, Course, Enrollment, User) {
        $scope.sortType = 'id'; // set the default sort type
        $scope.sortReverse = false; // set the default sort order
        $scope.searchFilter = ''; // set the default search/filter term
        $scope.load = function(courseId) {
            Course.enrollments({
                id: courseId
            }, function(result) {
                $scope.enrollments = result;
            });

        };
        $scope.delete = function(id) {
            Enrollment.get({
                id: id
            }, function(result) {
                $scope.enrollment = result;
                $('#deleteEnrollmentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function(id) {
            Enrollment.delete({
                    id: id
                },
                function() {
                    $scope.loadAll();
                    $('#deleteEnrollmentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.openProfile = function(id) {
            User.profile({
                id: id
            }, function(profile) {
                $state.go('userProfile.detail', {
                    id: profile.id
                });
            });
        };
        $scope.clear = function() {
            $scope.enrollment = null;
        }
        $scope.load($stateParams.id);
    });

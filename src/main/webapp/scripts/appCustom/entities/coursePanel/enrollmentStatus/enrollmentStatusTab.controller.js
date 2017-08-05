'use strict';

angular.module('buenOjoApp')
    .controller('EnrollmentStatusTabController', function($state, $stateParams, $scope, $q, entity, CourseInformation ) {
        /* $('.nav-tabs a').click(function (e) {
        	    e.preventDefault();
        	    $(this).tab('show');
        	});*/

        $scope.courseId = $state.params.id;
        $scope.load = function(id) {
            $scope.enrollment = entity;
            $q.all([$scope.enrollment.$promise]).then(function(){
              if (!$scope.enrollment){
                return $q.reject();
              }
              return $scope.enrollment;
            }).then(function(){
              $scope.course = $scope.enrollment.course;
              $scope.loadCourseInformation($scope.course.id);
            });
        };

        $scope.load($state.params.id);

        $scope.loadCourseInformation = function(courseId) {
            CourseInformation.user({
                    id: courseId,
                    userId:  $scope.enrollment.user.id
                },
                function(result) {
                    $scope.courseInformation = result;
                }
            );
        };


    });

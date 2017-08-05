'use strict';

angular.module('buenOjoApp')
    .controller('courseTabController', function($state, $stateParams,$q, $scope,$timeout, CourseInformation, Course, Activity, EnrollmentCurrentUserAndCourse, ActivityRouter) {
        /* $('.nav-tabs a').click(function (e) {
        	    e.preventDefault();
        	    $(this).tab('show');
        	});*/

        $scope.courseId = $state.params.id;
        $scope.showDonut = false
        // $scope.loadCourseInformation($state.params.id);
        $scope.courseInformation = CourseInformation.get({
                id: $scope.courseId
            }
        );
        $q.when($scope.courseInformation.$promise).then(  function(result) {

            $scope.showDonut = true;

            $timeout(function(){
              $scope.donutPercentage = result.approvedPercentage;
              if (result.exerciseCount > 0) {
                  $scope.startButtonText = "SEGUÍ PERFECCIONÁNDOTE";
              } else {
                  $scope.startButtonText = "COMENZAR";
              }
            },10);

          });
        $scope.load = function(id) {
            $scope.enrollment = $stateParams.enrollment;

            // fallback tratar de salvar el estado
            if (!$scope.enrollment) {
                $scope.enrollmentPromise =EnrollmentCurrentUserAndCourse.get({id: $scope.courseId},function(enrollment){
                  $scope.enrollment = enrollment;
                  $scope.course = enrollment.course;
                }).$promise;

            }else {
              $scope.course = $scope.enrollment.course;
            }
            //   Course.get({
            //       id: id
            //   }, function(result) {
            //       $scope.course = result;
            //       $scope.enrollment =EnrollmentCurrentUserAndCourse.get({id: $scope.courseId}).$promise;
            //   });
            // } else {
            //   $scope.course = $scope.enrollment.course;
            //
            // }

        };
        $scope.startButtonText = ""
        $scope.load($state.params.id);

        $scope.loadCourseInformation = function(courseId) {

        };
        $scope.start = function() {
            Activity.next({
                    won: false,
                    courseId: $scope.course.id
                },
                function(transition) {
                    $scope.transition = transition;
                    ActivityRouter.performTransition($scope.enrollment, transition);
                });
        };

         });

angular.module('buenOjoApp').directive('showtab',
    function() {
        return {
            link: function(scope, element, attrs) {
                element.click(function(e) {
                    e.preventDefault();
                    $(element).tab('show');
                });
            }
        };
    });

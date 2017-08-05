'use strict';

angular.module('buenOjoApp')
    .controller('CourseControllerList', function($state, $scope, Course, EnrollmentCurrentUserAndCourse, EnrollmentCurrentUser, CourseLevelSessionEnforceEnrollment, CourseUtils) {
        $scope.courses = [];
        $scope.enrollments = [];
        $scope.enrollmentMap = {};
        $scope.getCourseName = function(courses, id) {
            var i;
            for (i = 0;
                (courses[i].id != id && i < courses.length); i++);
            return courses[i].name;
        }

        $scope.startCourse = function(courseId) {
            var enrollment = $scope.enrollmentMap[courseId];
            if (enrollment == undefined || enrollment.status == undefined) {
                CourseLevelSessionEnforceEnrollment.query({
                    courseId: courseId
                }, function(result) {
                    var session = result.find(function (element,index,array){
                        return (element.courseLevelMap && element.courseLevelMap.parent == null);
                    });
                    EnrollmentCurrentUser.query({id: courseId},function(e){
                      // FIXME: no funciona el pasaje de parmetros

                      enrollment = e.find(function (element,index,array){
                          return (element.course.id == courseId && element.status == 'InProgress' || element.status == 'Started');
                      });
                      $state.go("courseTab", {
                          id: courseId,  // fix: la url queda incompleta sin este parametro

                          enrollment: enrollment
                      });
                    });
                });
            } else {
                $state.go("courseTab", {
                    id: courseId,
                    enrollment: enrollment
                });
            }
        }

        $scope.translateEnrollmentStatusToAction = function(status) {
            return CourseUtils.translateEnrollmentStatusToAction(status);
        }

        $scope.courseEnabled = function(enrollment) {
          if (!enrollment) return true;

          switch (enrollment.status) {
              case 'Started':
              case 'InProgress':
              case undefined:
                  return true;
                  break;
              case 'Cancelled':
              case 'Finished':
                  return false;
                  break;

          }
        };

        $scope.loadAllEnrollmentsOfCurrentUser = function() {
            EnrollmentCurrentUser.query(function(result) {
                $scope.enrollments = result;
                var i;
                for (i = 0; i < $scope.enrollments.length; i++) {
                    $scope.enrollmentMap[$scope.enrollments[i].course.id] = $scope.enrollments[i];
                }
                $scope.courses.forEach(function(element,index,array){
                  var enrollment = $scope.enrollmentMap[element.id];
                  element.enabled = $scope.courseEnabled(enrollment);
                });
            });
        };

        $scope.loadAllCourses = function() {
            Course.query(function(result) {
                $scope.courses = result;
                $scope.loadAllEnrollmentsOfCurrentUser();
            });
        };

        $scope.loadAllCourses();

        $scope.refresh = function() {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function() {
            $scope.course = {
                name: null,
                description: null,
                startDate: null,
                endDate: null,
                courseOpen: null,
                id: null
            };
        };
    });

'use strict';

angular.module('buenOjoApp')
    .controller('ControlPanelController', function ($scope, Course, CourseUtils) {
        $scope.courses = [];
        $scope.loadAll = function() {
            Course.query(function(result) {
               $scope.courses = result;
            });
        };
        $scope.loadAll();

        $scope.closeCourse = function (course) {

            $scope.course = course;
            $('#closeCourseConfirmation').modal('show');

        };

        $scope.confirmClose = function (course) {

          course.courseOpen = false;
          Course.update(course,function () {
              // $scope.loadAll();
              $('#closeCourseConfirmation').modal('hide');
              $scope.clear();
          });


        };
        $scope.statusForCourse = function(course){
            return CourseUtils.statusForCourse(course);
        };
        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.course = {
                name: null,
                description: null,
                startDate: null,
                endDate: null,
                courseOpen: null,
                id: null
            };
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:courseUpdate', result);

        };

        $scope.openCourse = function(course) {
            course.courseOpen = true;
            Course.update(course,onSaveFinished);
        };

        $scope.close = function (course) {
            $scope.course = course;
            $('#closeCourseConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Exercise.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#closeCourseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

    });

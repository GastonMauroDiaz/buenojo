'use strict';

angular.module('buenOjoApp')
    .controller('CoursePanelController', function ($scope, $state, $stateParams, Course,Enrollment) {
        $scope.course = $stateParams.course;
        $scope.load = function() {
            if ($scope.course) return;
            Course.get({id: $stateParams.id},function(result) {
               $scope.course = result;
            });
        };
        $scope.load();

        $scope.delete = function (id) {
            Course.get({id: id}, function(result) {
                $scope.course = result;
                $('#deleteCourseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Course.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCourseConfirmation').modal('hide');
                    $scope.clear();
                });
        };
        $scope.statusForCourse = function(course){
          if (course.courseOpen){
            return "Abierto";
          } else {
            return "Cerrado";
          }
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

        $scope.delete = function (id) {
            Exercise.get({id: id}, function(result) {
                $scope.exercise = result;
                $('#deleteExerciseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Exercise.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExerciseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

    });

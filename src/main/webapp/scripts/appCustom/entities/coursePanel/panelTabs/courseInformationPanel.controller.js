'use strict';

angular.module('buenOjoApp')
    .controller('CourseInformationPanelController', function ($scope, $state, $stateParams, Course, CourseUtils) {

      $scope.statusForCourse = CourseUtils.statusForCourse;

      $scope.translateEnrollmentStatusToAction = CourseUtils.translateEnrollmentStatusToAction;

      
    });

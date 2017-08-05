'use strict';

angular.module('buenOjoApp')
    .controller('CourseFinishedController', function($scope, $state, $stateParams, EnrollmentCurrentUser,CourseInformation) {



      CourseInformation.get({id: $scope.enrollment.course.id},function(result) {
        $scope.courseInformation = result;
        $scope.donutPercentage = result.approvedPercentage;
      });


      $scope.finish = function() {
        EnrollmentCurrentUser.finish($scope.enrollment,function(){

          $scope.modalInstance.dismiss('cancel');



        });
      };
});

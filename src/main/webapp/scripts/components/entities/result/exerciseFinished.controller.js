angular.module('buenOjoApp')
    .controller('ExerciseFinishedController', function($scope, $state, $stateParams) {


      $scope.setup = function (){


        $scope.message = $stateParams.message;
        $scope.courseLevelSession = $stateParams.courseLevelSession;
        $scope.count = $scope.courseLevelSession.exerciseCompletedCount;
        $scope.experience = $scope.courseLevelSession.experiencePoints;
        $scope.donutPercentage = $scope.courseLevelSession.percentage;
        $scope.levelName = $scope.enrollment.currentLevel.name;

      };
      $scope.setup();
      $scope.keepLearning = function() {
          if (!$scope.enrollment) { // salir si no hay enrollment
              $state.go('courseslist', {
                  force: true
              });
              $scope.modalInstance.dismiss('cancel');
              console.log("ResultController - keepLearning - No Enrollment");
              return;
          }
          $scope.performTransition();

      };

      $scope.clear = function() {
          $scope.modalInstance.dismiss('cancel');
          $state.go('courseslist', {
              force: true
          });
      };

});

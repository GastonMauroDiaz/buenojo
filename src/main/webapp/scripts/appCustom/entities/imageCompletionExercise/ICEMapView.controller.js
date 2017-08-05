
angular.module('buenOjoApp')
    .controller('ICEMapViewController', function ($scope, entity, $state) {
      var exercise = $scope.imageCompletionExercise;
      $scope.showScore = function() {
         $state.go('imageCompletionScore',{experience: 10, percentage: 25, currentCount: 1 });
      };




});

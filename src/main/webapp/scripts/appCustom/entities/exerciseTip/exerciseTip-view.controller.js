
angular.module('buenOjoApp')
    .controller('ExerciseTipViewController', function ($scope, entity) {

        // user parent scope on exerciseTip in order to update parent view
        $scope.itemsPerRow = 4;
        $scope.showControls = false;
        $scope.load = function() {
          $scope.showControls =  $scope.itemsPerRow < $scope.$parent.exerciseTip.images.length;
        }
        $scope.range = function(min, max, step) {
          step = step || 1;
          var input = [];
          for (var i = min; i <= max; i += step) {
              input.push(i);
          }
          return input;
        };

        $scope.showImage = function (index){
          $scope.currentImageUrl = $scope.$parent.exerciseTip.images[index].loResImagePath;
        }
        $scope.closeImage = function(){
          $scope.currentImageUrl = undefined;
        }
        $scope.closeTip = function (){
          $scope.$parent.exerciseTip = undefined;

        };
});

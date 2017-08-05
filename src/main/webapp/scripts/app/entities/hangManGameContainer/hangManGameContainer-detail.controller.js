'use strict';

angular.module('buenOjoApp')
    .controller('HangManGameContainerDetailExerciseController', function ($scope, $rootScope, $stateParams, entity, HangManGameContainer) {
        
    	  $scope.hangManExercises = [];
          $scope.loadAll = function() {
              HangManExercise.query(function(result) {
                 $scope.hangManExercises = result;
              });
          };
          $scope.loadAll();


          $scope.refresh = function () {
              $scope.loadAll();
              $scope.clear();
          };
    	

    });

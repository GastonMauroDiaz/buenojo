'use strict';

angular.module('buenOjoApp')
    .controller('HangManGameContainerDetailExerciseController', function ($scope, $rootScope, $stateParams, entity, HangManGameContainer, HangManExerciseByContainer) {
    	 $scope.hangManExercises = [];
    	 
    	    
         $scope.loadAll = function(id) {
        	 HangManExerciseByContainer.get({id: id}, function(result) {
                $scope.hangManExercises = result;
             });
         };
         $scope.loadAll($stateParams.id);


         $scope.refresh = function () {
             $scope.loadAll($stateParams.id);
             $scope.clear();
         };

    });

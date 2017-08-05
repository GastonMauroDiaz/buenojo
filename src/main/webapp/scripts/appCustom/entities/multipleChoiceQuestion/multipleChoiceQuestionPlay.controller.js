'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceQuestionPlayController', function ($scope, $state, $stateParams,entity, MultipleChoiceQuestion, MultipleChoiceAnswer, MultipleChoiceGamePlay,MultipleChoiceAnswerByQuery) {
    	   $scope.multipleChoiceAnswers = [];
    	   $scope.isSingle=false;
           $scope.loadAllByQuery = function(id) {
        	   MultipleChoiceGamePlay.get({id: id},function(result) {
                  $scope.multipleChoiceAnswers = result.answers;
                  $scope.isSingle= result.single;
               });
           };
           $scope.loadAllByQuery($stateParams.id);
    	
    	 $scope.multipleChoiceQuestion = entity;
         $scope.load = function (id) {
        	
             MultipleChoiceQuestion.get({id: id}, function(result) {
                
            	 $scope.multipleChoiceQuestion = result;
             });
         };
      
        $scope.load($stateParams.id);


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.multipleChoiceQuestion = {
                question: null,
                id: null
            };
        };
    });

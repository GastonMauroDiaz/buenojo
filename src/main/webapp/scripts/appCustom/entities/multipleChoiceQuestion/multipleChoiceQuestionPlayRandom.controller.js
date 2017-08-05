'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceQuestionPlayRandomController', function ($scope, $state, $stateParams, MultipleChoiceQuestion, MultipleChoiceAnswer, MultipleChoiceGamePlay, MultipleChoiceGamePlayRandom,MultipleChoiceAnswerByQuery) {
    	   $scope.multipleChoiceAnswers = [];
    	   $scope.isSingle=false;
    	   $scope.multipleChoiceQuestion = null;
    	   $scope.loadAllByQuery = function() {
        	   MultipleChoiceGamePlayRandom.get(function(result) {
                  $scope.multipleChoiceAnswers = result.answers;
                  $scope.isSingle= result.single;
                  $scope.multipleChoiceQuestion= result.multipleChoiceQuestion;
              });
           };
           $scope.loadAllByQuery();
    	
    	

        $scope.refresh = function () {
        	
            $scope.loadAllByQuery();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.multipleChoiceQuestion = {
                question: null,
                id: null
            };
        };
    });

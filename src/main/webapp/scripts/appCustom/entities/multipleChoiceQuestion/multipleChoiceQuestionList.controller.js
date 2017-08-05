'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceQuestionListController', function ($scope, $state, $modal, $stateParams, MultipleChoiceQuestion, MultipleChoiceQuestionByContainer) {
      
        
    	$scope.multipleChoiceQuestions = [];
        $scope.loadAll = function() {
        	if ($stateParams.id==undefined)
        	MultipleChoiceQuestion.query(function(result) {
               $scope.multipleChoiceQuestions = result;
            });
        else
        	MultipleChoiceQuestionByContainer.query({id: $stateParams.id},function(result) {
                $scope.multipleChoiceQuestions = result;
             });
        	
        };
        $scope.loadAll();


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

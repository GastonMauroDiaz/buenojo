'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceQuestionController', function ($scope, $state, $modal, MultipleChoiceQuestion) {
      
        $scope.multipleChoiceQuestions = [];
        $scope.loadAll = function() {
            MultipleChoiceQuestion.query(function(result) {
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
                interactionType: null,
                exerciseId: null,
                source: null,
                id: null
            };
        };
    });

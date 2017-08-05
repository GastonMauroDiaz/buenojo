'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceAnswerController', function ($scope, $state, $modal, MultipleChoiceAnswer) {
      
        $scope.multipleChoiceAnswers = [];
        $scope.loadAll = function() {
            MultipleChoiceAnswer.query(function(result) {
               $scope.multipleChoiceAnswers = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.multipleChoiceAnswer = {
                answer: null,
                isRight: false,
                id: null
            };
        };
    });

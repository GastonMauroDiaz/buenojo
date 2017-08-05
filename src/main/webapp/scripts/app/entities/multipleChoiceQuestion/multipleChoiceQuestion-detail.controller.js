'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceQuestionDetailController', function ($scope, $rootScope, $stateParams, entity, MultipleChoiceQuestion, MultipleChoiceExerciseContainer, ImageResource, MultipleChoiceSubjectSpecific) {
        $scope.multipleChoiceQuestion = entity;
        $scope.load = function (id) {
            MultipleChoiceQuestion.get({id: id}, function(result) {
                $scope.multipleChoiceQuestion = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:multipleChoiceQuestionUpdate', function(event, result) {
            $scope.multipleChoiceQuestion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

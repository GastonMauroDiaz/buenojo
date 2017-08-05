'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceAnswerDetailController', function ($scope, $rootScope, $stateParams, entity, MultipleChoiceAnswer, MultipleChoiceQuestion) {
        $scope.multipleChoiceAnswer = entity;
        $scope.load = function (id) {
            MultipleChoiceAnswer.get({id: id}, function(result) {
                $scope.multipleChoiceAnswer = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:multipleChoiceAnswerUpdate', function(event, result) {
            $scope.multipleChoiceAnswer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceQuestionImageResourceDetailController', function ($scope, $rootScope, $stateParams, entity, MultipleChoiceQuestionImageResource, MultipleChoiceQuestion, ImageResource) {
        $scope.multipleChoiceQuestionImageResource = entity;
        $scope.load = function (id) {
            MultipleChoiceQuestionImageResource.get({id: id}, function(result) {
                $scope.multipleChoiceQuestionImageResource = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:multipleChoiceQuestionImageResourceUpdate', function(event, result) {
            $scope.multipleChoiceQuestionImageResource = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

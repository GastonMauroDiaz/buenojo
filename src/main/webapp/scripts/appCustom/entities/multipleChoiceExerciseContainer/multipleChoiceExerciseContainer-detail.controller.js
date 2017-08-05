'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceExerciseContainerDetailController', function ($scope, $rootScope, $stateParams, entity, MultipleChoiceExerciseContainer, MultipleChoiceQuestion) {
        $scope.multipleChoiceExerciseContainer = entity;
        $scope.load = function (id) {
            MultipleChoiceExerciseContainer.get({id: id}, function(result) {
                $scope.multipleChoiceExerciseContainer = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:multipleChoiceExerciseContainerUpdate', function(event, result) {
            $scope.multipleChoiceExerciseContainer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

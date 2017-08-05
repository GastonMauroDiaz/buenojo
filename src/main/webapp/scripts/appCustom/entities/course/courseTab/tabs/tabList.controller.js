'use strict';

angular.module('buenOjoApp')
    .controller('tabListController', function ($scope, $state, $modal, MultipleChoiceExerciseContainer) {
      
        $scope.multipleChoiceExerciseContainers = [];
        $scope.loadAll = function() {
            MultipleChoiceExerciseContainer.query(function(result) {
               $scope.multipleChoiceExerciseContainers = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.multipleChoiceExerciseContainer = {
                name: null,
                id: null
            };
        };
    });

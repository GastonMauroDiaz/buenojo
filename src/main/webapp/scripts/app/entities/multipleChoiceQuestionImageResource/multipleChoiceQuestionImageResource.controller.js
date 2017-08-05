'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceQuestionImageResourceController', function ($scope, $state, $modal, MultipleChoiceQuestionImageResource) {
      
        $scope.multipleChoiceQuestionImageResources = [];
        $scope.loadAll = function() {
            MultipleChoiceQuestionImageResource.query(function(result) {
               $scope.multipleChoiceQuestionImageResources = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.multipleChoiceQuestionImageResource = {
                id: null
            };
        };
    });

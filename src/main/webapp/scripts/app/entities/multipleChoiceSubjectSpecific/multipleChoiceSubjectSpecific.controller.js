'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceSubjectSpecificController', function ($scope, $state, $modal, MultipleChoiceSubjectSpecific) {
      
        $scope.multipleChoiceSubjectSpecifics = [];
        $scope.loadAll = function() {
            MultipleChoiceSubjectSpecific.query(function(result) {
               $scope.multipleChoiceSubjectSpecifics = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.multipleChoiceSubjectSpecific = {
                text: null,
                id: null
            };
        };
    });

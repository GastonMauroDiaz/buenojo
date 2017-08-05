'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceSubjectController', function ($scope, $state, $modal, MultipleChoiceSubject) {
      
        $scope.multipleChoiceSubjects = [];
        $scope.loadAll = function() {
            MultipleChoiceSubject.query(function(result) {
               $scope.multipleChoiceSubjects = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.multipleChoiceSubject = {
                text: null,
                id: null
            };
        };
    });

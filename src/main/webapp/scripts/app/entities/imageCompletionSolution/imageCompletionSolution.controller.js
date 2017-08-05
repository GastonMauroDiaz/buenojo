'use strict';

angular.module('buenOjoApp')
    .controller('ImageCompletionSolutionController', function ($scope, ImageCompletionSolution) {
        $scope.imageCompletionSolutions = [];
        $scope.loadAll = function() {
            ImageCompletionSolution.query(function(result) {
               $scope.imageCompletionSolutions = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ImageCompletionSolution.get({id: id}, function(result) {
                $scope.imageCompletionSolution = result;
                $('#deleteImageCompletionSolutionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ImageCompletionSolution.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteImageCompletionSolutionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.imageCompletionSolution = {
                id: null
            };
        };
    });

'use strict';

angular.module('buenOjoApp')
    .controller('TagPoolController', function ($scope, TagPool) {
        $scope.tagPools = [];
        $scope.loadAll = function() {
            TagPool.query(function(result) {
               $scope.tagPools = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TagPool.get({id: id}, function(result) {
                $scope.tagPool = result;
                $('#deleteTagPoolConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TagPool.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTagPoolConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tagPool = {
                similarity: null,
                id: null
            };
        };
    });

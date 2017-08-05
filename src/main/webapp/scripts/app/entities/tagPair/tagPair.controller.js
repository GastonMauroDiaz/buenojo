'use strict';

angular.module('buenOjoApp')
    .controller('TagPairController', function ($scope, TagPair) {
        $scope.tagPairs = [];
        $scope.loadAll = function() {
            TagPair.query(function(result) {
               $scope.tagPairs = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TagPair.get({id: id}, function(result) {
                $scope.tagPair = result;
                $('#deleteTagPairConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TagPair.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTagPairConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tagPair = {
                tagSlotId: null,
                id: null
            };
        };
    });

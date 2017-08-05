'use strict';

angular.module('buenOjoApp')
    .controller('TagCircleController', function ($scope, TagCircle) {
        $scope.tagCircles = [];
        $scope.loadAll = function() {
            TagCircle.query(function(result) {
               $scope.tagCircles = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TagCircle.get({id: id}, function(result) {
                $scope.tagCircle = result;
                $('#deleteTagCircleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TagCircle.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTagCircleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tagCircle = {
                number: null,
                y: null,
                x: null,
                radioPx: null,
                id: null
            };
        };
    });

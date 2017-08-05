'use strict';

angular.module('buenOjoApp')
    .controller('TagController', function ($scope, Tag) {
        $scope.tags = [];
        $scope.loadAll = function() {
            Tag.query(function(result) {
               $scope.tags = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Tag.get({id: id}, function(result) {
                $scope.tag = result;
                $('#deleteTagConfirmation').modal('show');
            });
        };
        $scope.deleteAll = function () {
            
                $('#deleteAllTagsConfirmation').modal('show');
            
        };
        $scope.confirmDeleteAll = function () {
            Tag.delete({},
                function () {
                    $scope.loadAll();
                    $('#deleteAllTagsConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.confirmDelete = function (id) {
            Tag.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTagConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tag = {
                name: null,
                id: null
            };
        };
    });

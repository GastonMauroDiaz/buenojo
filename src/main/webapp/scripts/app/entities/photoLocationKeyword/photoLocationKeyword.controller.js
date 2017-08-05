'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationKeywordController', function ($scope, PhotoLocationKeyword) {
        $scope.photoLocationKeywords = [];
        $scope.loadAll = function() {
            PhotoLocationKeyword.query(function(result) {
               $scope.photoLocationKeywords = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PhotoLocationKeyword.get({id: id}, function(result) {
                $scope.photoLocationKeyword = result;
                $('#deletePhotoLocationKeywordConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PhotoLocationKeyword.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoLocationKeywordConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photoLocationKeyword = {
                name: null,
                description: null,
                id: null
            };
        };
    });

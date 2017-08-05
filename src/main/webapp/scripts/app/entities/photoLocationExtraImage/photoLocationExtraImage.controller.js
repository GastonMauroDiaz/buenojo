'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationExtraImageController', function ($scope, PhotoLocationExtraImage) {
        $scope.photoLocationExtraImages = [];
        $scope.loadAll = function() {
            PhotoLocationExtraImage.query(function(result) {
               $scope.photoLocationExtraImages = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PhotoLocationExtraImage.get({id: id}, function(result) {
                $scope.photoLocationExtraImage = result;
                $('#deletePhotoLocationExtraImageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PhotoLocationExtraImage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoLocationExtraImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photoLocationExtraImage = {
                id: null
            };
        };
    });

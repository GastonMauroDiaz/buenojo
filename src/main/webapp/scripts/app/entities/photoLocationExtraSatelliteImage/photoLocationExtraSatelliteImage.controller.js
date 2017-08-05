'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationExtraSatelliteImageController', function ($scope, PhotoLocationExtraSatelliteImage) {
        $scope.photoLocationExtraSatelliteImages = [];
        $scope.loadAll = function() {
            PhotoLocationExtraSatelliteImage.query(function(result) {
               $scope.photoLocationExtraSatelliteImages = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PhotoLocationExtraSatelliteImage.get({id: id}, function(result) {
                $scope.photoLocationExtraSatelliteImage = result;
                $('#deletePhotoLocationExtraSatelliteImageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PhotoLocationExtraSatelliteImage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoLocationExtraSatelliteImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photoLocationExtraSatelliteImage = {
                id: null
            };
        };
    });

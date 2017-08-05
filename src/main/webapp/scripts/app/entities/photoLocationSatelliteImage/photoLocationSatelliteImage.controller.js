'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationSatelliteImageController', function ($scope, PhotoLocationSatelliteImage) {
        $scope.photoLocationSatelliteImages = [];
        $scope.loadAll = function() {
            PhotoLocationSatelliteImage.query(function(result) {
               $scope.photoLocationSatelliteImages = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PhotoLocationSatelliteImage.get({id: id}, function(result) {
                $scope.photoLocationSatelliteImage = result;
                $('#deletePhotoLocationSatelliteImageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PhotoLocationSatelliteImage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoLocationSatelliteImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photoLocationSatelliteImage = {
                id: null
            };
        };
    });

'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationBeaconController', function ($scope, PhotoLocationBeacon) {
        $scope.photoLocationBeacons = [];
        $scope.loadAll = function() {
            PhotoLocationBeacon.query(function(result) {
               $scope.photoLocationBeacons = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PhotoLocationBeacon.get({id: id}, function(result) {
                $scope.photoLocationBeacon = result;
                $('#deletePhotoLocationBeaconConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PhotoLocationBeacon.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoLocationBeaconConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photoLocationBeacon = {
                x: null,
                y: null,
                tolerance: null,
                id: null
            };
        };
    });

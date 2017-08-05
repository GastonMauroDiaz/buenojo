'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationSightPairController', function ($scope, PhotoLocationSightPair) {
        $scope.photoLocationSightPairs = [];
        $scope.loadAll = function() {
            PhotoLocationSightPair.query(function(result) {
               $scope.photoLocationSightPairs = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PhotoLocationSightPair.get({id: id}, function(result) {
                $scope.photoLocationSightPair = result;
                $('#deletePhotoLocationSightPairConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PhotoLocationSightPair.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoLocationSightPairConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photoLocationSightPair = {
                number: null,
                satelliteX: null,
                satelliteY: null,
                satelliteTolerance: null,
                terrainX: null,
                terrainY: null,
                terrainTolerance: null,
                id: null
            };
        };
    });

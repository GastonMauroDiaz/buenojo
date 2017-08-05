'use strict';

angular.module('buenOjoApp')
    .controller('SatelliteImageController', function ($scope, SatelliteImage) {
        $scope.satelliteImages = [];
        $scope.loadAll = function() {
            SatelliteImage.query(function(result) {
               $scope.satelliteImages = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SatelliteImage.get({id: id}, function(result) {
                $scope.satelliteImage = result;
                $('#deleteSatelliteImageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SatelliteImage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSatelliteImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.satelliteImage = {
                meters: null,
                lon: null,
                lat: null,
                resolution: null,
                name: null,
                image: null,
                imageContentType: null,
                copyright: null,
                imageType: null,
                id: null
            };
        };

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
        };

        $scope.byteSize = function (base64String) {
            if (!angular.isString(base64String)) {
                return '';
            }
            function endsWith(suffix, str) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }
            function paddingSize(base64String) {
                if (endsWith('==', base64String)) {
                    return 2;
                }
                if (endsWith('=', base64String)) {
                    return 1;
                }
                return 0;
            }
            function size(base64String) {
                return base64String.length / 4 * 3 - paddingSize(base64String);
            }
            function formatAsBytes(size) {
                return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
            }

            return formatAsBytes(size(base64String));
        };
    });

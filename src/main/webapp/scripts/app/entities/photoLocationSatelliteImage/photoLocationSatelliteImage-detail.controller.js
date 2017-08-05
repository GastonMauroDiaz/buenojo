'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationSatelliteImageDetailController', function ($scope, $rootScope, $stateParams, entity, PhotoLocationSatelliteImage, PhotoLocationKeyword, SatelliteImage) {
        $scope.photoLocationSatelliteImage = entity;
        $scope.load = function (id) {
            PhotoLocationSatelliteImage.get({id: id}, function(result) {
                $scope.photoLocationSatelliteImage = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:photoLocationSatelliteImageUpdate', function(event, result) {
            $scope.photoLocationSatelliteImage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

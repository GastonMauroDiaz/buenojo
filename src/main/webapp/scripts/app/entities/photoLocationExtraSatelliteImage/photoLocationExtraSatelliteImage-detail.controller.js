'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationExtraSatelliteImageDetailController', function ($scope, $rootScope, $stateParams, entity, PhotoLocationExtraSatelliteImage, PhotoLocationSatelliteImage, Course) {
        $scope.photoLocationExtraSatelliteImage = entity;
        $scope.load = function (id) {
            PhotoLocationExtraSatelliteImage.get({id: id}, function(result) {
                $scope.photoLocationExtraSatelliteImage = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:photoLocationExtraSatelliteImageUpdate', function(event, result) {
            $scope.photoLocationExtraSatelliteImage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

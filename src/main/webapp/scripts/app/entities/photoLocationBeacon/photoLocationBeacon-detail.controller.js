'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationBeaconDetailController', function ($scope, $rootScope, $stateParams, entity, PhotoLocationBeacon, PhotoLocationExercise) {
        $scope.photoLocationBeacon = entity;
        $scope.load = function (id) {
            PhotoLocationBeacon.get({id: id}, function(result) {
                $scope.photoLocationBeacon = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:photoLocationBeaconUpdate', function(event, result) {
            $scope.photoLocationBeacon = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationSightPairDetailController', function ($scope, $rootScope, $stateParams, entity, PhotoLocationSightPair, PhotoLocationExercise) {
        $scope.photoLocationSightPair = entity;
        $scope.load = function (id) {
            PhotoLocationSightPair.get({id: id}, function(result) {
                $scope.photoLocationSightPair = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:photoLocationSightPairUpdate', function(event, result) {
            $scope.photoLocationSightPair = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationExtraImageDetailController', function ($scope, $rootScope, $stateParams, entity, PhotoLocationExtraImage, PhotoLocationImage, Course) {
        $scope.photoLocationExtraImage = entity;
        $scope.load = function (id) {
            PhotoLocationExtraImage.get({id: id}, function(result) {
                $scope.photoLocationExtraImage = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:photoLocationExtraImageUpdate', function(event, result) {
            $scope.photoLocationExtraImage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

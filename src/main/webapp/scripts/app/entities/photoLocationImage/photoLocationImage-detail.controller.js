'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationImageDetailController', function ($scope, $rootScope, $stateParams, entity, PhotoLocationImage, ImageResource, PhotoLocationKeyword) {
        $scope.photoLocationImage = entity;
        $scope.load = function (id) {
            PhotoLocationImage.get({id: id}, function(result) {
                $scope.photoLocationImage = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:photoLocationImageUpdate', function(event, result) {
            $scope.photoLocationImage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

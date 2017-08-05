'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationKeywordDetailController', function ($scope, $rootScope, $stateParams, entity, PhotoLocationKeyword) {
        $scope.photoLocationKeyword = entity;
        $scope.load = function (id) {
            PhotoLocationKeyword.get({id: id}, function(result) {
                $scope.photoLocationKeyword = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:photoLocationKeywordUpdate', function(event, result) {
            $scope.photoLocationKeyword = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('buenOjoApp')
    .controller('TagPoolDetailController', function ($scope, $rootScope, $stateParams, entity, TagPool, Tag) {
        $scope.tagPool = entity;
        $scope.load = function (id) {
            TagPool.get({id: id}, function(result) {
                $scope.tagPool = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:tagPoolUpdate', function(event, result) {
            $scope.tagPool = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

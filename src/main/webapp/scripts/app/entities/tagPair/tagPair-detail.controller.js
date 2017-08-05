'use strict';

angular.module('buenOjoApp')
    .controller('TagPairDetailController', function ($scope, $rootScope, $stateParams, entity, TagPair, Tag, ImageCompletionSolution) {
        $scope.tagPair = entity;
        $scope.load = function (id) {
            TagPair.get({id: id}, function(result) {
                $scope.tagPair = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:tagPairUpdate', function(event, result) {
            $scope.tagPair = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

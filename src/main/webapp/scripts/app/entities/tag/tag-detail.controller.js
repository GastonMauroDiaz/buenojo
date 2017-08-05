'use strict';

angular.module('buenOjoApp')
    .controller('TagDetailController', function ($scope, $rootScope, $stateParams, entity, Tag, TagPair, Course) {
        $scope.tag = entity;
        $scope.load = function (id) {
            Tag.get({id: id}, function(result) {
                $scope.tag = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:tagUpdate', function(event, result) {
            $scope.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('buenOjoApp')
    .controller('HangManOptionListItemDetailController', function ($scope, $rootScope, $stateParams, entity, HangManOptionListItem) {
        $scope.hangManOptionListItem = entity;
        $scope.load = function (id) {
            HangManOptionListItem.get({id: id}, function(result) {
                $scope.hangManOptionListItem = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:hangManOptionListItemUpdate', function(event, result) {
            $scope.hangManOptionListItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

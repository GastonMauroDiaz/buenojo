'use strict';

angular.module('buenOjoApp')
    .controller('HangManOptionListItemController', function ($scope, $state, $modal, HangManOptionListItem) {
      
        $scope.hangManOptionListItems = [];
        $scope.loadAll = function() {
            HangManOptionListItem.query(function(result) {
               $scope.hangManOptionListItems = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.hangManOptionListItem = {
                optionGroup: null,
                optionType: null,
                optionText: null,
                id: null
            };
        };
    });

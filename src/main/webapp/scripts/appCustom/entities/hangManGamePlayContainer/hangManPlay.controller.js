'use strict';
 
angular.module('buenOjoApp')
    .controller('HangManGameContainerController', function ($scope, $state, $modal, HangManGameContainer) {
      
        $scope.hangManGameContainers = [];
        $scope.loadAll = function() {
            HangManGameContainer.query(function(result) {
               $scope.hangManGameContainers = result;
            });
        }; 
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.hangManGameContainer = {
                name: null,
                id: null
            };
        };
    });

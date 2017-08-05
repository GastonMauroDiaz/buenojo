'use strict';

angular.module('buenOjoApp')
    .controller('LevelController', function ($scope, $state, $modal, Level) {
      
        $scope.levels = [];
        $scope.loadAll = function() {
            Level.query(function(result) {
               $scope.levels = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.level = {
                levelOrder: null,
                name: null,
                description: null,
                id: null
            };
        };
    });

'use strict';

angular.module('buenOjoApp')
    .controller('CourseLevelMapController', function ($scope, $state, $modal, CourseLevelMap) {
      
        $scope.courseLevelMaps = [];
        $scope.loadAll = function() {
            CourseLevelMap.query(function(result) {
               $scope.courseLevelMaps = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.courseLevelMap = {
                id: null
            };
        };
    });

'use strict';

angular.module('buenOjoApp')
    .controller('CourseLevelSessionController', function ($scope, $state, $modal, CourseLevelSession) {
      
        $scope.courseLevelSessions = [];
        $scope.loadAll = function() {
            CourseLevelSession.query(function(result) {
               $scope.courseLevelSessions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.courseLevelSession = {
                status: null,
                percentage: null,
                experiencePoints: null,
                exerciseCompletedCount: null,
                id: null
            };
        };
    });

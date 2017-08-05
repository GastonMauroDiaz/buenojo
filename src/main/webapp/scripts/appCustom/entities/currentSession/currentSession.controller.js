'use strict';

angular.module('buenOjoApp')
    .controller('CurrentSessionCustomController', function ($scope, $state, $modal, CurrentSession, CurrentSessionCustom) {
      
        $scope.currentSessions = [];
        $scope.loadAll = function() {
            CurrentSession.query(function(result) {
               $scope.currentSessions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.currentSession = {
                id: null
            };
        };
    });

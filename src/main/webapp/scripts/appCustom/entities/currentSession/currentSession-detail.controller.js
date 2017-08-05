'use strict';

angular.module('buenOjoApp')
    .controller('CurrentSessionDetailCustomController', function ($scope, $rootScope, $stateParams, entity, CurrentSession, User, CourseLevelSession) {
        $scope.currentSession = entity;
        $scope.load = function (id) {
            CurrentSession.get({id: id}, function(result) {
                $scope.currentSession = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:currentSessionUpdate', function(event, result) {
            $scope.currentSession = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('buenOjoApp')
    .controller('LevelDetailController', function ($scope, $rootScope, $stateParams, entity, Level, Course, Exercise,Activity) {
        $scope.level = entity;
        $scope.level.$promise.then(function(){
          Activity.level({levelId: entity.id}, function(activities){
            $scope.level.activities = activities;
          });
        });

        $scope.load = function (id) {
            Level.get({id: id}, function(result) {
                $scope.level = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:levelUpdate', function(event, result) {
            $scope.level = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

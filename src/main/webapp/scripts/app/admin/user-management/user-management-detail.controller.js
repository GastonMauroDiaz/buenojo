'use strict';

angular.module('buenOjoApp')
    .controller('UserManagementDetailController', function ($scope, $stateParams, User, Authorities) {
        $scope.user = {};
        $scope.authorities = {};
        $scope.load = function (login) {
            User.get({login: login}, function(result) {
                $scope.user = result;
            });

            Authority.query(function(result){
                $scope.authorities = result;
              
            });

        };
        $scope.load($stateParams.login);
    });

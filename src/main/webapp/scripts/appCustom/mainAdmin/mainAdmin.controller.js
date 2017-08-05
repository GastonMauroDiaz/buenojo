'use strict';

angular.module('buenOjoApp')
    .controller('MainAdminController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });

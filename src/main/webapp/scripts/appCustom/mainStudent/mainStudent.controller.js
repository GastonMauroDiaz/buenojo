'use strict';

angular.module('buenOjoApp')
    .controller('MainStudentController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });

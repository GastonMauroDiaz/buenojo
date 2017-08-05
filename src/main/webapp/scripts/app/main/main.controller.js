'use strict';

angular.module('buenOjoApp')
    .controller('MainController', function ($scope, Principal, $state) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            if (account!=null){
            if (account.authorities.indexOf('ROLE_COURSE_ADMIN')>-1) $state.go('controlPanel',{},{reload:true});
            if (account.authorities.indexOf('ROLE_COURSE_STUDENT')>-1) $state.go('courseslist', {},{reload:true});
            }
          });

    });

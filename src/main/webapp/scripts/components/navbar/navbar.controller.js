'use strict';

angular.module('buenOjoApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal,RegisterModal,LoginModal,UserProfile, ENV) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';
        $scope.userProfile = {};

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
        $scope.loginUser = function () {
            LoginModal();
        };

        $scope.registerUser = function () {
            RegisterModal();
        };
        $scope.dataset =function(){
          $state.go('dataSet');
        };
        Principal.identity().then(function(account) {
            $scope.account = account;
            if (Principal.isAuthenticated === true) {
              UserProfile.mine({},function(profile){
                  $scope.userProfile = profile;
              });
            }

          });


});

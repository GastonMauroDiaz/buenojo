'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('register', {
                parent: 'account',
                url: '/register',
                data: {
                    authorities: [],
                    pageTitle: 'Registration'
                },
                /*views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/register/register.html',
                        controller: 'RegisterController'
                    }
                },*/
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/appCustom/account/register/register.html',
                        controller: 'RegisterController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                                                  };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('login', null, { reload: true });
                    }, function() {
                        $state.go('register');
                    })
                }]
            });
    });

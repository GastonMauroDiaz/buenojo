'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('login', {
                parent: 'account',
                url: '/login',
                data: {
                    authorities: [],
                    pageTitle: 'Sign in'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/account/login/login.html',
                        controller: 'LoginController'
                    }
                },
                resolve: {

                }
            });
    });

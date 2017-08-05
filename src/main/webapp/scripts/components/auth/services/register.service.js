'use strict';

angular.module('buenOjoApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



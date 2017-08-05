'use strict';

angular.module('buenOjoApp')
    .factory('User', function ($resource) {
        return $resource('api/users/:login', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'update': { method:'PUT' },
                'profile': {
                  method: 'GET',
                  transformResponse: function (data) {
                      data = angular.fromJson(data);
                      return data;
                  },
                  url: 'api/users/:id/profile'
                }
            });
        });

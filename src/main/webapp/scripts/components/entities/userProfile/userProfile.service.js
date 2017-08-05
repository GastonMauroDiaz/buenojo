'use strict';

angular.module('buenOjoApp')
    .factory('UserProfile', function ($resource, DateUtils) {
        return $resource('api/userProfiles/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'mine': {
              method: 'GET',
              transformResponse: function (data) {
                  data = angular.fromJson(data);
                  return data;
              },
              url: 'api/myProfile'
            }
        });
    });

'use strict';

angular.module('buenOjoApp')
    .factory('HangManGameContainer', function ($resource, DateUtils) {
        return $resource('api/hangManGameContainers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
            
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

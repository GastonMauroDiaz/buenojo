'use strict';

angular.module('buenOjoApp')
    .factory('HangManOptionListItem', function ($resource, DateUtils) {
        return $resource('api/hangManOptionListItems/:id', {}, {
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

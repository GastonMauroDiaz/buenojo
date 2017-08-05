'use strict';

angular.module('buenOjoApp')
    .factory('CurrentSession', function ($resource, DateUtils) {
        return $resource('api/currentSessions/:id', {}, {
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

angular.module('buenOjoApp')
.factory('CurrentSessionCustom', function ($resource, DateUtils) {
    return $resource('api/currentSessionsCustom/:id', {}, {
     });
});

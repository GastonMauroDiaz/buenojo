'use strict';

angular.module('buenOjoApp')
    .factory('PhotoLocationExtraImageExercise', function ($resource, DateUtils) {
        return $resource('api/photoLocationImages/extra/exercise/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

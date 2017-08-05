'use strict';

angular.module('buenOjoApp')
    .factory('MultipleChoiceSubjectSpecific', function ($resource, DateUtils) {
        return $resource('api/multipleChoiceSubjectSpecifics/:id', {}, {
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

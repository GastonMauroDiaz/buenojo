'use strict';

angular.module('buenOjoApp')
    .factory('MultipleChoiceQuestion', function ($resource, DateUtils) {
        return $resource('api/multipleChoiceQuestions/:id', {}, {
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
    })
.factory('MultipleChoiceQuestionByContainer', function ($resource, DateUtils) {
    return $resource('api/multipleChoiceQuestionsByContainer/:id', {}, {
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

'use strict';

angular.module('buenOjoApp')
    .factory('MultipleChoiceAnswer', function ($resource, DateUtils) {
        return $resource('api/multipleChoiceAnswers/:id', {}, {
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
    }).factory('MultipleChoiceAnswerByContainer', function ($resource, DateUtils) {
        return $resource('api/multipleChoiceAnswersByContainer/:id', {}, {
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


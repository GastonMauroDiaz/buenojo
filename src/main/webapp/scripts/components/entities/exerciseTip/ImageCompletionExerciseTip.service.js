'use strict';

angular.module('buenOjoApp')
    .factory('ImageCompletionExerciseTip', function ($resource, DateUtils) {
        return $resource('api/exerciseTips/random/:id/satelliteImage/:image', {}, {

            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data.length === 0) return {};
                    data = angular.fromJson(data);
                    return data;
                }
            },

        });
    });

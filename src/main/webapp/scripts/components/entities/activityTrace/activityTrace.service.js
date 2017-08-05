'use strict';

angular.module('buenOjoApp')
    .factory('ActivityTrace', function ($resource, DateUtils) {
        return $resource('api/activityTraces/:id', {}, {

            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                    data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    return data;
                },
            },
            'update': { method:'PUT' },
            'start': {
                method:'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                    data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    return data;
                },
                url: 'api/activityTraces/:activityId/start/'
            },
            'end': {
              method: 'PUT',
              transformResponse: function (data) {
                  data = angular.fromJson(data);
                  data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                  data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                  return data;
              },
              url: 'api/activityTraces/end'
            },
            'queryByEnrollment': {
              method: 'GET',
              isArray: true,
              url: 'api/activityTraces/enrollment/:enrollmentId'
            }

        });
    });

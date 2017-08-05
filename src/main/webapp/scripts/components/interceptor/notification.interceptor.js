 'use strict';

angular.module('buenOjoApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-buenOjoApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-buenOjoApp-params')});
                }
                return response;
            }
        };
    });

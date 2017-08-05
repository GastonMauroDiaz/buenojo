'use strict';

angular.module('buenOjoApp')
    .controller('LoaderTraceDetailController', function ($scope, $rootScope, $stateParams, entity, LoaderTrace) {
        $scope.loaderTrace = entity;
        $scope.load = function (id) {
            LoaderTrace.get({id: id}, function(result) {
                $scope.loaderTrace = result;
            });
        };
        $rootScope.$on('buenOjoApp:loaderTraceUpdate', function(event, result) {
            $scope.loaderTrace = result;
        });
    });

'use strict';

angular.module('buenOjoApp')
    .controller('ActivityTraceController', function ($scope, ActivityTrace, ParseLinks) {
        $scope.activityTraces = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ActivityTrace.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.activityTraces = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ActivityTrace.get({id: id}, function(result) {
                $scope.activityTrace = result;
                $('#deleteActivityTraceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ActivityTrace.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteActivityTraceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.activityTrace = {
                startDate: null,
                endDate: null,
                score: null,
                id: null
            };
        };
    });

'use strict';

angular.module('buenOjoApp')
    .controller('LoaderTraceController', function ($scope, LoaderTrace, ParseLinks) {
        $scope.loaderTraces = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            LoaderTrace.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.loaderTraces = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            LoaderTrace.get({id: id}, function(result) {
                $scope.loaderTrace = result;
                $('#deleteLoaderTraceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            LoaderTrace.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLoaderTraceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.loaderTrace = {
                loaderResult: null,
                author: null,
                loaderType: null,
                date: null,
                resultLog: null,
                datasetName: null,
                id: null
            };
        };
    });

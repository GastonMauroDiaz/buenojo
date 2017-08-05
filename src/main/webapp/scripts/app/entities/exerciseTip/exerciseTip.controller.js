'use strict';

angular.module('buenOjoApp')
    .controller('ExerciseTipController', function ($scope, ExerciseTip) {
        $scope.exerciseTips = [];
        $scope.loadAll = function() {
            ExerciseTip.query(function(result) {
               $scope.exerciseTips = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ExerciseTip.get({id: id}, function(result) {
                $scope.exerciseTip = result;
                $('#deleteExerciseTipConfirmation').modal('show');
            });
        };
        $scope.deleteAll = function (id) {
        	 $('#deleteAllExerciseTipsConfirmation').modal('show');
        };
        $scope.confirmDeleteAll = function () {
            ExerciseTip.delete({},
                function () {
                    $scope.loadAll();
                    $('#deleteAllExerciseTipsConfirmation').modal('hide');
                    $scope.clear();
                });
        };
        $scope.confirmDelete = function (id) {
            ExerciseTip.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExerciseTipConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.exerciseTip = {
                hint: null,
                tipDetail: null,
                id: null
            };
        };
    });

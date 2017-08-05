'use strict';

angular.module('buenOjoApp')
    .controller('CourseController', function ($scope, Course) {
        $scope.courses = [];

        $scope.sortType     = 'id'; // set the default sort type
        $scope.sortReverse  = false;  // set the default sort order
        $scope.searchFilter  = '';     // set the default search/filter term

        $scope.loadAll = function() {
            Course.query(function(result) {
               $scope.courses = result;
            });
        };
        $scope.loadAll();
    
        $scope.delete = function (id) {
            Course.get({id: id}, function(result) {
                $scope.course = result;
                $('#deleteCourseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Course.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCourseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.course = {
                name: null,
                description: null,
                startDate: null,
                endDate: null,
                courseOpen: null,
                id: null
            };
        };
    });

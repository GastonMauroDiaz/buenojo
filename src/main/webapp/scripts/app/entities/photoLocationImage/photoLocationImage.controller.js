'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationImageController', function ($scope, PhotoLocationImage) {
        $scope.photoLocationImages = [];
        $scope.loadAll = function() {
            PhotoLocationImage.query(function(result) {
               $scope.photoLocationImages = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PhotoLocationImage.get({id: id}, function(result) {
                $scope.photoLocationImage = result;
                $('#deletePhotoLocationImageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PhotoLocationImage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhotoLocationImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.photoLocationImage = {
                id: null
            };
        };
    });

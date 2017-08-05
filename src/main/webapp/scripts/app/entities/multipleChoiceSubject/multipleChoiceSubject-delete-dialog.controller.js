'use strict';

angular.module('buenOjoApp')
	.controller('MultipleChoiceSubjectDeleteController', function($scope, $modalInstance, entity, MultipleChoiceSubject) {

        $scope.multipleChoiceSubject = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MultipleChoiceSubject.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
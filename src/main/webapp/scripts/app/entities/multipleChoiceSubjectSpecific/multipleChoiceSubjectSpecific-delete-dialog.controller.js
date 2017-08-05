'use strict';

angular.module('buenOjoApp')
	.controller('MultipleChoiceSubjectSpecificDeleteController', function($scope, $modalInstance, entity, MultipleChoiceSubjectSpecific) {

        $scope.multipleChoiceSubjectSpecific = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MultipleChoiceSubjectSpecific.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });
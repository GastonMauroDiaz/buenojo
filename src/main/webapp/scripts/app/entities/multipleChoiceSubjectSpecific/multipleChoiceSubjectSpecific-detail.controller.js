'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceSubjectSpecificDetailController', function ($scope, $rootScope, $stateParams, entity, MultipleChoiceSubjectSpecific, MultipleChoiceSubject) {
        $scope.multipleChoiceSubjectSpecific = entity;
        $scope.load = function (id) {
            MultipleChoiceSubjectSpecific.get({id: id}, function(result) {
                $scope.multipleChoiceSubjectSpecific = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:multipleChoiceSubjectSpecificUpdate', function(event, result) {
            $scope.multipleChoiceSubjectSpecific = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('buenOjoApp')
    .controller('MultipleChoiceSubjectDetailController', function ($scope, $rootScope, $stateParams, entity, MultipleChoiceSubject) {
        $scope.multipleChoiceSubject = entity;
        $scope.load = function (id) {
            MultipleChoiceSubject.get({id: id}, function(result) {
                $scope.multipleChoiceSubject = result;
            });
        };
        var unsubscribe = $rootScope.$on('buenOjoApp:multipleChoiceSubjectUpdate', function(event, result) {
            $scope.multipleChoiceSubject = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

'use strict';

angular.module('buenOjoApp').controller('PhotoLocationKeywordDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PhotoLocationKeyword','Course',
        function($scope, $stateParams, $modalInstance, entity, PhotoLocationKeyword, Course) {
        $scope.courses = Course.query();
        $scope.photoLocationKeyword = entity;
        $scope.load = function(id) {
            PhotoLocationKeyword.get({id : id}, function(result) {
                $scope.photoLocationKeyword = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationKeywordUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.photoLocationKeyword.id != null) {
                PhotoLocationKeyword.update($scope.photoLocationKeyword, onSaveFinished);
            } else {
                PhotoLocationKeyword.save($scope.photoLocationKeyword, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

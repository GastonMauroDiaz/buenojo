'use strict';

angular.module('buenOjoApp').controller('CourseLevelSessionDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'CourseLevelSession', 'CourseLevelMap', 'User',
        function($scope, $stateParams, $modalInstance, $q, entity, CourseLevelSession, CourseLevelMap, User) {

        $scope.courseLevelSession = entity;
        $scope.courselevelmaps = CourseLevelMap.query({filter: 'courselevelsession-is-null'});
        $q.all([$scope.courseLevelSession.$promise, $scope.courselevelmaps.$promise]).then(function() {
            if (!$scope.courseLevelSession.courseLevelMap) {
                return $q.reject();
            }
            return CourseLevelMap.get({id : $scope.courseLevelSession.courseLevelMap.id}).$promise;
        }).then(function(courseLevelMap) {
            $scope.courselevelmaps.push(courseLevelMap);
        });
        $scope.users = User.query();
        $scope.load = function(id) {
            CourseLevelSession.get({id : id}, function(result) {
                $scope.courseLevelSession = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:courseLevelSessionUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.courseLevelSession.id != null) {
                CourseLevelSession.update($scope.courseLevelSession, onSaveSuccess, onSaveError);
            } else {
                CourseLevelSession.save($scope.courseLevelSession, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

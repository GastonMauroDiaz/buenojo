'use strict';

angular.module('buenOjoApp').controller('CurrentSessionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CurrentSession', 'User', 'CourseLevelSession',
        function($scope, $stateParams, $modalInstance, entity, CurrentSession, User, CourseLevelSession) {

        $scope.currentSession = entity;
        $scope.users = User.query();
        $scope.courselevelsessions = CourseLevelSession.query();
        $scope.load = function(id) {
            CurrentSession.get({id : id}, function(result) {
                $scope.currentSession = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('buenOjoApp:currentSessionUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.currentSession.id != null) {
                CurrentSession.update($scope.currentSession, onSaveSuccess, onSaveError);
            } else {
                CurrentSession.save($scope.currentSession, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

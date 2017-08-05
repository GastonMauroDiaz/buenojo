'use strict';

angular.module('buenOjoApp').controller('CurrentSessionCustomDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CurrentSession', 'User', 'CourseLevelSession', 'CurrentSessionCustom',
        function($scope, $stateParams, $modalInstance, entity, CurrentSession, User, CourseLevelSession, CurrentSessionCustom) {

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
        
        $scope.saveCustom = function () {
            $scope.isSaving = true;
            debugger;
            alert(1);
            if ($scope.currentSession.id != null) {
                CurrentSessionCustom.update($scope.currentSession, onSaveSuccess, onSaveError);
            } else {
                CurrentSessionCustom.save($scope.currentSession, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

'use strict';

angular.module('buenOjoApp').controller('UserProfileDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'UserProfile', 'User',
        function($scope, $stateParams, $modalInstance, entity, UserProfile, User) {

        $scope.userProfile = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            UserProfile.get({id : id}, function(result) {
                $scope.userProfile = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:userProfileUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.userProfile.id != null) {
                UserProfile.update($scope.userProfile, onSaveFinished);
            } else {
                UserProfile.save($scope.userProfile, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

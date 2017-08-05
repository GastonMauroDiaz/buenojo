'use strict';

angular.module('buenOjoApp').controller('ActivityTraceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ActivityTrace', 'Enrollment', 'Activity',
        function($scope, $stateParams, $modalInstance, entity, ActivityTrace, Enrollment, Activity) {

        $scope.activityTrace = entity;
        $scope.enrollments = Enrollment.query();
        $scope.activities = Activity.query();
        $scope.load = function(id) {
            ActivityTrace.get({id : id}, function(result) {
                $scope.activityTrace = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:activityTraceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.activityTrace.id != null) {
                ActivityTrace.update($scope.activityTrace, onSaveFinished);
            } else {
                ActivityTrace.save($scope.activityTrace, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

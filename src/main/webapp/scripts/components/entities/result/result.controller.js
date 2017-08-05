'use strict';

angular.module('buenOjoApp')
    .controller('ResultController', ['$scope', '$state', '$stateParams', '$modalInstance', 'entity', 'Activity', 'ActivityRouter', function($scope, $state, $stateParams, $modalInstance, entity, Activity, ActivityRouter) {

        $scope.enrollment = $stateParams.enrollment;
        $scope.modalInstance = $modalInstance;
        $scope.showDialog = false;
        $scope.courseLevelSession = $stateParams.courseLevelSession;
        $scope.donutPercentage = $scope.courseLevelSession.approvedPercentage;
        $scope.load = function() {

            Activity.next({
                    won: $stateParams.won,
                    courseId: $scope.enrollment.course.id
                },
                function(transition) {
                    $scope.transition = transition;

                    $scope.levelUp = transition.levelUp;
                    $scope.showDialog = true;
                });



        };
        $scope.load();


        $scope.performTransition = function() {
            ActivityRouter.performTransition($scope.enrollment, $scope.transition);
            $modalInstance.close();
        };


    }]);

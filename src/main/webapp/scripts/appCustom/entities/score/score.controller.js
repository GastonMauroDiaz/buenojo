'use strict';

angular.module('buenOjoApp')
.controller('ScoreController',
		['$scope', '$stateParams', '$modalInstance', 'entity',    function($scope, $stateParams, $modalInstance,entity) {
$scope.count= $stateParams.count;
$scope.experience= $stateParams.experience;

$scope.donutPercentage= $stateParams.percentage;
if ($stateParams.levelNumber!=null) 
	{
	$scope.levelNumber=$stateParams.levelNumber;
	$scope.levelName=$stateParams.levelName;
	}
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

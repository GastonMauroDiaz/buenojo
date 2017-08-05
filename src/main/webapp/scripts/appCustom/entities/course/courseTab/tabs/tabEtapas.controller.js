'use strict';

angular.module('buenOjoApp')
    .controller('tabEtapasController', function ($state, $scope,$q) {


        $q.when($scope.courseInformation.$promise).then(function(result){
          $scope.showDonut = true;
          $scope.donutPercentage = result.approvedPercentage;

        });

            });

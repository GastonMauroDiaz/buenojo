'use strict';

angular.module('buenOjoApp')
    .controller('DataSetController', function ($scope,$state, $q, ExerciseDataSet, ActivityType) {

        $scope.dataSets = ExerciseDataSet.query();

        $scope.generate = function(dto){
            $state.go('dataSet.confirm', { dataset: dto });
        };

        $scope.describe = ActivityType.describe;
        $scope.title = function(dto){
            if (dto.result === "Done") return "exitosa"
            return "fallida"
        };
        $scope.subtitle = function(dto){
          if (dto.result === "Done") return "";
          if (dto.result === "Unknown") return "Error desconocido";
          if (dto.result === "Failed") return "Error de carga";
          return "";
        };

    });

'use strict';

angular.module('buenOjoApp').controller('DataSetConfirmController', ['$scope', '$state', '$stateParams','$timeout', 'Course', 'ExerciseDataSet',
    function($scope, $state, $stateParams, $timeout, Course, ExerciseDataSet) {

        $scope.dataset = $stateParams.dataset;

        $scope.courses = Course.query();
        $scope.resultDTO = {};
        $scope.selectedCourse = {};
        $scope.loadExtraResources = true;
        $scope.save = function(isDryRun) {
            ExerciseDataSet.load({
                courseId: $scope.selectedCourse.id,
                dryRun: isDryRun,
                loadExtra: $scope.loadExtraResources 
            }, $scope.dataset, function(result) {
                $timeout(function(){
                  $scope.resultDTO = result;
                },10)
                console.log(result);
            });
        };

        $scope.message = function() {
            if (!$scope.resultDTO) return ""
            return "El error ocurrio en la fecha " + new Date() + ". Para el set de datos \n" + $scope.resultDTO.dataSet.name + "\n en la ruta " + $scope.resultDTO.dataSet.path + ". \nCopie este aviso y envíelo al administrador";
        };

        $scope.title = function(dto) {
            if (!dto) return "";
            if (dto.result === "Done") return "exitosa"
            return "fallida"
        };
        $scope.subtitle = function(dto) {
            if (!dto) return "";
            if (dto.result === "Done") return "";
            if (dto.result === "Unknown") return "Error desconocido";
            if (dto.result === "Failed") return "Error de carga";
            return "";
        };

    }
]);

'use strict';

angular.module('buenOjoApp')
    .controller('LoaderResultController', function ($scope) {


        $scope.message = function() {
          if (!$scope.resultDTO) return ""
          return "El error ocurrio en la fecha "+new Date()+". Para el set de datos \n"+$scope.resultDTO.dataSet.name+"\n en la ruta "+ $scope.resultDTO.dataSet.path+". \nCopie este aviso y envíelo al administrador";
        };

        $scope.title = function(dto){
          if (!dto) return "";
            if (dto.result === "Done") return "exitosa"
            return "fallida"
        };
        $scope.subtitle = function(dto){
          if (!dto) return "";
          if (dto.result === "Done") return "";
          if (dto.result === "Unknown") return "Error desconocido";
          if (dto.result === "Failed") return "Error de carga";
          return "";
        };

    });

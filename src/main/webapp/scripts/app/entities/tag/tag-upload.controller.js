'use strict';

angular.module('buenOjoApp').controller('TagUploadController',
    ['$scope','Upload', '$stateParams', '$modalInstance','Course', 
        function($scope,Upload, $stateParams, $modalInstance,Course) {
    		$scope.courses = Course.query();
    	 var onSaveFinished = function (result) {
             $scope.$emit('buenOjoApp:tagUpdate', result);
             $modalInstance.close(result);
         };
    	
    	 // upload on file select or drop
        $scope.upload = function (file,course) {
            Upload.upload({
                url: ('api/upload/tagPool/'+course.id),
                file: file
            }).then(function (resp) {
                
                $modalInstance.close(resp);
                onSaveFinished(resp);
            }, function (resp) {
                
                $modalInstance.close(resp);
                onSaveFinished(resp);
            }, function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                
                
            });
        };
       
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        
}]);

'use strict';

angular.module('buenOjoApp').controller('SatelliteImageUploadController',
    ['$scope','Upload', 'entity','$stateParams', '$modalInstance','Course', 
        function($scope,Upload,entity, $stateParams, $modalInstance,Course) {
    		$scope.satelliteImageUpload = entity;
    		$scope.courses = Course.query();
    	 var onSaveFinished = function (result) {
             $scope.$emit('buenOjoApp:satelliteImageUpdate', result);
//             $modalInstance.close(result);
         };
         $scope.uploadFiles = function (files) {
             $scope.files = files;
             if (files && files.length) {
                 Upload.upload({
                     url: 'api/satelliteImages/upload',
                     data: {
                         files: files
                     }
                 }).then(function (response) {
                     $timeout(function () {
                         $scope.result = response.data;
                     });
                 }, function (response) {
                     if (response.status > 0) {
                         $scope.errorMsg = response.status + ': ' + response.data;
                     }
                 }, function (evt) {
                     $scope.progress = 
                         Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                 });
             }
         };
    	 // upload on file select or drop
         $scope.upload = function (imageFile,csvFile) {
             Upload.upload({
                 url: 'api/satelliteImages/upload',
                 fileFormDataName: ['imageFile','metadata'],
                 file: [imageFile,csvFile]
                 
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

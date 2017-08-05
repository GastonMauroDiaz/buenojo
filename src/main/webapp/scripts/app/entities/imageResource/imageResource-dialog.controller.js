'use strict';

angular.module('buenOjoApp').controller('ImageResourceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ImageResource',
        function($scope, $stateParams, $modalInstance, entity, ImageResource) {

        $scope.imageResource = entity;
        $scope.load = function(id) {
            ImageResource.get({id : id}, function(result) {
                $scope.imageResource = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:imageResourceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.imageResource.id != null) {
                ImageResource.update($scope.imageResource, onSaveFinished);
            } else {
                ImageResource.save($scope.imageResource, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
        };

        $scope.byteSize = function (base64String) {
            if (!angular.isString(base64String)) {
                return '';
            }
            function endsWith(suffix, str) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }
            function paddingSize(base64String) {
                if (endsWith('==', base64String)) {
                    return 2;
                }
                if (endsWith('=', base64String)) {
                    return 1;
                }
                return 0;
            }
            function size(base64String) {
                return base64String.length / 4 * 3 - paddingSize(base64String);
            }
            function formatAsBytes(size) {
                return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
            }

            return formatAsBytes(size(base64String));
        };

        $scope.setLoResImage = function ($file, imageResource) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                    	if (!imageResource.name) imageResource.name = $file.name;
                        imageResource.loResImage = base64Data;
                        imageResource.loResImageContentType = $file.type;
                    });
                };
            }
        };

        $scope.setHiResImage = function ($file, imageResource) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                    	  if (!imageResource.name) imageResource.name = $file.name;
                        imageResource.hiResImage = base64Data;
                        imageResource.hiResImageContentType = $file.type;
                    });
                };
            }
        };
}]);

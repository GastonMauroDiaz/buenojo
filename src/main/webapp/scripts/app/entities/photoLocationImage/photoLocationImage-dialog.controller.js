'use strict';

angular.module('buenOjoApp').controller('PhotoLocationImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'PhotoLocationImage', 'ImageResource', 'PhotoLocationKeyword',
        function($scope, $stateParams, $modalInstance, $q, entity, PhotoLocationImage, ImageResource, PhotoLocationKeyword) {

        $scope.photoLocationImage = entity;

        $scope.images = ImageResource.query({filter: 'photolocationimage-is-null'});
        $q.all([$scope.photoLocationImage.$promise, $scope.images.$promise]).then(function() {
            if (!$scope.photoLocationImage.image.id) {
                return $q.reject();
            }
            return ImageResource.get({id : $scope.photoLocationImage.image.id}).$promise;
        }).then(function(image) {
            $scope.images.push(image);
        });
        $scope.photolocationkeywords = PhotoLocationKeyword.query();
        $scope.load = function(id) {
            PhotoLocationImage.get({id : id}, function(result) {

                $scope.photoLocationImage = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationImageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.photoLocationImage.id != null) {
                PhotoLocationImage.update($scope.photoLocationImage, onSaveFinished);
            } else {
                PhotoLocationImage.save($scope.photoLocationImage, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

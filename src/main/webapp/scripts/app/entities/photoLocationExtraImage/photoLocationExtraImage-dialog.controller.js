'use strict';

angular.module('buenOjoApp').controller('PhotoLocationExtraImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'PhotoLocationExtraImage', 'PhotoLocationImage', 'Course',
        function($scope, $stateParams, $modalInstance, $q, entity, PhotoLocationExtraImage, PhotoLocationImage, Course) {

        $scope.photoLocationExtraImage = entity;
        $scope.images = PhotoLocationImage.query({filter: 'photolocationextraimage-is-null'});
        $q.all([$scope.photoLocationExtraImage.$promise, $scope.images.$promise]).then(function() {
            if (!$scope.photoLocationExtraImage.image.id) {
                return $q.reject();
            }
            return PhotoLocationImage.get({id : $scope.photoLocationExtraImage.image.id}).$promise;
        }).then(function(image) {
            $scope.images.push(image);
        });
        $scope.courses = Course.query({filter: 'photolocationextraimage-is-null'});
        $q.all([$scope.photoLocationExtraImage.$promise, $scope.courses.$promise]).then(function() {
            if (!$scope.photoLocationExtraImage.course.id) {
                return $q.reject();
            }
            return Course.get({id : $scope.photoLocationExtraImage.course.id}).$promise;
        }).then(function(course) {
            $scope.courses.push(course);
        });
        $scope.load = function(id) {
            PhotoLocationExtraImage.get({id : id}, function(result) {
                $scope.photoLocationExtraImage = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('buenOjoApp:photoLocationExtraImageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.photoLocationExtraImage.id != null) {
                PhotoLocationExtraImage.update($scope.photoLocationExtraImage, onSaveFinished);
            } else {
                PhotoLocationExtraImage.save($scope.photoLocationExtraImage, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

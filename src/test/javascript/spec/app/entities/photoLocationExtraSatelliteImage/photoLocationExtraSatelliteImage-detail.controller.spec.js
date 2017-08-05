'use strict';

describe('PhotoLocationExtraSatelliteImage Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPhotoLocationExtraSatelliteImage, MockPhotoLocationSatelliteImage, MockCourse;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPhotoLocationExtraSatelliteImage = jasmine.createSpy('MockPhotoLocationExtraSatelliteImage');
        MockPhotoLocationSatelliteImage = jasmine.createSpy('MockPhotoLocationSatelliteImage');
        MockCourse = jasmine.createSpy('MockCourse');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PhotoLocationExtraSatelliteImage': MockPhotoLocationExtraSatelliteImage,
            'PhotoLocationSatelliteImage': MockPhotoLocationSatelliteImage,
            'Course': MockCourse
        };
        createController = function() {
            $injector.get('$controller')("PhotoLocationExtraSatelliteImageDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:photoLocationExtraSatelliteImageUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

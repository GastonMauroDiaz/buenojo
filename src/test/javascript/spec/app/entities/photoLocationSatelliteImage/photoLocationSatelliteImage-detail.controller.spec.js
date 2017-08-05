'use strict';

describe('PhotoLocationSatelliteImage Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPhotoLocationSatelliteImage, MockPhotoLocationKeyword, MockSatelliteImage;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPhotoLocationSatelliteImage = jasmine.createSpy('MockPhotoLocationSatelliteImage');
        MockPhotoLocationKeyword = jasmine.createSpy('MockPhotoLocationKeyword');
        MockSatelliteImage = jasmine.createSpy('MockSatelliteImage');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PhotoLocationSatelliteImage': MockPhotoLocationSatelliteImage,
            'PhotoLocationKeyword': MockPhotoLocationKeyword,
            'SatelliteImage': MockSatelliteImage
        };
        createController = function() {
            $injector.get('$controller')("PhotoLocationSatelliteImageDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:photoLocationSatelliteImageUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

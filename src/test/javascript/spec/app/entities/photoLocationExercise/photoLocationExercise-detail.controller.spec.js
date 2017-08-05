'use strict';

describe('PhotoLocationExercise Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPhotoLocationExercise, MockPhotoLocationBeacon, MockPhotoLocationSightPair, MockPhotoLocationKeyword, MockPhotoLocationImage, MockPhotoLocationSatelliteImage;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPhotoLocationExercise = jasmine.createSpy('MockPhotoLocationExercise');
        MockPhotoLocationBeacon = jasmine.createSpy('MockPhotoLocationBeacon');
        MockPhotoLocationSightPair = jasmine.createSpy('MockPhotoLocationSightPair');
        MockPhotoLocationKeyword = jasmine.createSpy('MockPhotoLocationKeyword');
        MockPhotoLocationImage = jasmine.createSpy('MockPhotoLocationImage');
        MockPhotoLocationSatelliteImage = jasmine.createSpy('MockPhotoLocationSatelliteImage');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PhotoLocationExercise': MockPhotoLocationExercise,
            'PhotoLocationBeacon': MockPhotoLocationBeacon,
            'PhotoLocationSightPair': MockPhotoLocationSightPair,
            'PhotoLocationKeyword': MockPhotoLocationKeyword,
            'PhotoLocationImage': MockPhotoLocationImage,
            'PhotoLocationSatelliteImage': MockPhotoLocationSatelliteImage
        };
        createController = function() {
            $injector.get('$controller')("PhotoLocationExerciseDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:photoLocationExerciseUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

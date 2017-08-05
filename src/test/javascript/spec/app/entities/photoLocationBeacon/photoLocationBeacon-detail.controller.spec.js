'use strict';

describe('PhotoLocationBeacon Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPhotoLocationBeacon, MockPhotoLocationExercise;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPhotoLocationBeacon = jasmine.createSpy('MockPhotoLocationBeacon');
        MockPhotoLocationExercise = jasmine.createSpy('MockPhotoLocationExercise');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PhotoLocationBeacon': MockPhotoLocationBeacon,
            'PhotoLocationExercise': MockPhotoLocationExercise
        };
        createController = function() {
            $injector.get('$controller')("PhotoLocationBeaconDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:photoLocationBeaconUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

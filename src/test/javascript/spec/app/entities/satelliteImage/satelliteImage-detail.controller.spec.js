'use strict';

describe('SatelliteImage Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSatelliteImage, MockImageCompletionExercise;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSatelliteImage = jasmine.createSpy('MockSatelliteImage');
        MockImageCompletionExercise = jasmine.createSpy('MockImageCompletionExercise');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'SatelliteImage': MockSatelliteImage,
            'ImageCompletionExercise': MockImageCompletionExercise
        };
        createController = function() {
            $injector.get('$controller')("SatelliteImageDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:satelliteImageUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

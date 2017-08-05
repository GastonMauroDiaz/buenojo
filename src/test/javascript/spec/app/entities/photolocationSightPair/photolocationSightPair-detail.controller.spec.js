'use strict';

describe('PhotoLocationSightPair Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPhotoLocationSightPair, MockPhotoLocationExercise;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPhotoLocationSightPair = jasmine.createSpy('MockPhotoLocationSightPair');
        MockPhotoLocationExercise = jasmine.createSpy('MockPhotoLocationExercise');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PhotoLocationSightPair': MockPhotoLocationSightPair,
            'PhotoLocationExercise': MockPhotoLocationExercise
        };
        createController = function() {
            $injector.get('$controller')("PhotoLocationSightPairDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:photoLocationSightPairUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

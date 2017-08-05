'use strict';

describe('HangManExerciseHint Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHangManExerciseHint, MockHangManExercise;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHangManExerciseHint = jasmine.createSpy('MockHangManExerciseHint');
        MockHangManExercise = jasmine.createSpy('MockHangManExercise');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HangManExerciseHint': MockHangManExerciseHint,
            'HangManExercise': MockHangManExercise
        };
        createController = function() {
            $injector.get('$controller')("HangManExerciseHintDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:hangManExerciseHintUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

'use strict';

describe('HangManExerciseDelimitedArea Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHangManExerciseDelimitedArea, MockHangManExercise;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHangManExerciseDelimitedArea = jasmine.createSpy('MockHangManExerciseDelimitedArea');
        MockHangManExercise = jasmine.createSpy('MockHangManExercise');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HangManExerciseDelimitedArea': MockHangManExerciseDelimitedArea,
            'HangManExercise': MockHangManExercise
        };
        createController = function() {
            $injector.get('$controller')("HangManExerciseDelimitedAreaDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:hangManExerciseDelimitedAreaUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

'use strict';

describe('HangManExerciseOption Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHangManExerciseOption, MockHangManExercise;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHangManExerciseOption = jasmine.createSpy('MockHangManExerciseOption');
        MockHangManExercise = jasmine.createSpy('MockHangManExercise');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HangManExerciseOption': MockHangManExerciseOption,
            'HangManExercise': MockHangManExercise
        };
        createController = function() {
            $injector.get('$controller')("HangManExerciseOptionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:hangManExerciseOptionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

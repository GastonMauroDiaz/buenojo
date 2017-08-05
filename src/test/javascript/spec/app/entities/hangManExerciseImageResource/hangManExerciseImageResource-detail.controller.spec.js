'use strict';

describe('HangManExerciseImageResource Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHangManExerciseImageResource, MockHangManExercise, MockImageResource;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHangManExerciseImageResource = jasmine.createSpy('MockHangManExerciseImageResource');
        MockHangManExercise = jasmine.createSpy('MockHangManExercise');
        MockImageResource = jasmine.createSpy('MockImageResource');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HangManExerciseImageResource': MockHangManExerciseImageResource,
            'HangManExercise': MockHangManExercise,
            'ImageResource': MockImageResource
        };
        createController = function() {
            $injector.get('$controller')("HangManExerciseImageResourceDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:hangManExerciseImageResourceUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

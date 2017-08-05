'use strict';

describe('HangManExercise Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHangManExercise, MockImageResource;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHangManExercise = jasmine.createSpy('MockHangManExercise');
        MockImageResource = jasmine.createSpy('MockImageResource');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HangManExercise': MockHangManExercise,
            'ImageResource': MockImageResource
        };
        createController = function() {
            $injector.get('$controller')("HangManExerciseDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:hangManExerciseUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

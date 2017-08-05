'use strict';

describe('TagCircle Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockTagCircle, MockImageCompletionExercise;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockTagCircle = jasmine.createSpy('MockTagCircle');
        MockImageCompletionExercise = jasmine.createSpy('MockImageCompletionExercise');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'TagCircle': MockTagCircle,
            'ImageCompletionExercise': MockImageCompletionExercise
        };
        createController = function() {
            $injector.get('$controller')("TagCircleDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:tagCircleUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

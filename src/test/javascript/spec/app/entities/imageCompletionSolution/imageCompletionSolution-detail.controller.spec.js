'use strict';

describe('ImageCompletionSolution Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockImageCompletionSolution, MockImageCompletionExercise, MockTagPair;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockImageCompletionSolution = jasmine.createSpy('MockImageCompletionSolution');
        MockImageCompletionExercise = jasmine.createSpy('MockImageCompletionExercise');
        MockTagPair = jasmine.createSpy('MockTagPair');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ImageCompletionSolution': MockImageCompletionSolution,
            'ImageCompletionExercise': MockImageCompletionExercise,
            'TagPair': MockTagPair
        };
        createController = function() {
            $injector.get('$controller')("ImageCompletionSolutionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:imageCompletionSolutionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

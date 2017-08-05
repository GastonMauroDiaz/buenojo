'use strict';

describe('ImageCompletionExercise Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockImageCompletionExercise, MockImageCompletionSolution, MockTag, MockSatelliteImage, MockTagCircle;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockImageCompletionExercise = jasmine.createSpy('MockImageCompletionExercise');
        MockImageCompletionSolution = jasmine.createSpy('MockImageCompletionSolution');
        MockTag = jasmine.createSpy('MockTag');
        MockSatelliteImage = jasmine.createSpy('MockSatelliteImage');
        MockTagCircle = jasmine.createSpy('MockTagCircle');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ImageCompletionExercise': MockImageCompletionExercise,
            'ImageCompletionSolution': MockImageCompletionSolution,
            'Tag': MockTag,
            'SatelliteImage': MockSatelliteImage,
            'TagCircle': MockTagCircle
        };
        createController = function() {
            $injector.get('$controller')("ImageCompletionExerciseDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:imageCompletionExerciseUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

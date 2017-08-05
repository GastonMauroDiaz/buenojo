'use strict';

describe('MultipleChoiceExerciseContainer Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMultipleChoiceExerciseContainer, MockMultipleChoiceQuestion;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMultipleChoiceExerciseContainer = jasmine.createSpy('MockMultipleChoiceExerciseContainer');
        MockMultipleChoiceQuestion = jasmine.createSpy('MockMultipleChoiceQuestion');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'MultipleChoiceExerciseContainer': MockMultipleChoiceExerciseContainer,
            'MultipleChoiceQuestion': MockMultipleChoiceQuestion
        };
        createController = function() {
            $injector.get('$controller')("MultipleChoiceExerciseContainerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:multipleChoiceExerciseContainerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

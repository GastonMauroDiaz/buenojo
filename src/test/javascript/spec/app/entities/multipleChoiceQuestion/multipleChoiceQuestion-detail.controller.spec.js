'use strict';

describe('MultipleChoiceQuestion Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMultipleChoiceQuestion, MockMultipleChoiceExerciseContainer, MockImageResource, MockMultipleChoiceSubjectSpecific;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMultipleChoiceQuestion = jasmine.createSpy('MockMultipleChoiceQuestion');
        MockMultipleChoiceExerciseContainer = jasmine.createSpy('MockMultipleChoiceExerciseContainer');
        MockImageResource = jasmine.createSpy('MockImageResource');
        MockMultipleChoiceSubjectSpecific = jasmine.createSpy('MockMultipleChoiceSubjectSpecific');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'MultipleChoiceQuestion': MockMultipleChoiceQuestion,
            'MultipleChoiceExerciseContainer': MockMultipleChoiceExerciseContainer,
            'ImageResource': MockImageResource,
            'MultipleChoiceSubjectSpecific': MockMultipleChoiceSubjectSpecific
        };
        createController = function() {
            $injector.get('$controller')("MultipleChoiceQuestionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:multipleChoiceQuestionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

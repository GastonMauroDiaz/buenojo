'use strict';

describe('MultipleChoiceAnswer Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMultipleChoiceAnswer, MockMultipleChoiceQuestion, MockImageResource;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMultipleChoiceAnswer = jasmine.createSpy('MockMultipleChoiceAnswer');
        MockMultipleChoiceQuestion = jasmine.createSpy('MockMultipleChoiceQuestion');
        MockImageResource = jasmine.createSpy('MockImageResource');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'MultipleChoiceAnswer': MockMultipleChoiceAnswer,
            'MultipleChoiceQuestion': MockMultipleChoiceQuestion,
            'ImageResource': MockImageResource
        };
        createController = function() {
            $injector.get('$controller')("MultipleChoiceAnswerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:multipleChoiceAnswerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

'use strict';

describe('MultipleChoiceQuestionImageResource Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMultipleChoiceQuestionImageResource, MockMultipleChoiceQuestion, MockImageResource;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMultipleChoiceQuestionImageResource = jasmine.createSpy('MockMultipleChoiceQuestionImageResource');
        MockMultipleChoiceQuestion = jasmine.createSpy('MockMultipleChoiceQuestion');
        MockImageResource = jasmine.createSpy('MockImageResource');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'MultipleChoiceQuestionImageResource': MockMultipleChoiceQuestionImageResource,
            'MultipleChoiceQuestion': MockMultipleChoiceQuestion,
            'ImageResource': MockImageResource
        };
        createController = function() {
            $injector.get('$controller')("MultipleChoiceQuestionImageResourceDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:multipleChoiceQuestionImageResourceUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

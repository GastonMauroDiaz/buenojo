'use strict';

describe('MultipleChoiceSubjectSpecific Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMultipleChoiceSubjectSpecific, MockMultipleChoiceSubject;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMultipleChoiceSubjectSpecific = jasmine.createSpy('MockMultipleChoiceSubjectSpecific');
        MockMultipleChoiceSubject = jasmine.createSpy('MockMultipleChoiceSubject');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'MultipleChoiceSubjectSpecific': MockMultipleChoiceSubjectSpecific,
            'MultipleChoiceSubject': MockMultipleChoiceSubject
        };
        createController = function() {
            $injector.get('$controller')("MultipleChoiceSubjectSpecificDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:multipleChoiceSubjectSpecificUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

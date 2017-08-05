'use strict';

describe('MultipleChoiceSubject Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMultipleChoiceSubject;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMultipleChoiceSubject = jasmine.createSpy('MockMultipleChoiceSubject');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'MultipleChoiceSubject': MockMultipleChoiceSubject
        };
        createController = function() {
            $injector.get('$controller')("MultipleChoiceSubjectDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:multipleChoiceSubjectUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

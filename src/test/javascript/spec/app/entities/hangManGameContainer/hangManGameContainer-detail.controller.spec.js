'use strict';

describe('HangManGameContainer Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHangManGameContainer;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHangManGameContainer = jasmine.createSpy('MockHangManGameContainer');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HangManGameContainer': MockHangManGameContainer
        };
        createController = function() {
            $injector.get('$controller')("HangManGameContainerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:hangManGameContainerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

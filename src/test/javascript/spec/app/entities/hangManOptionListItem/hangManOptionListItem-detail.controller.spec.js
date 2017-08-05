'use strict';

describe('HangManOptionListItem Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockHangManOptionListItem;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockHangManOptionListItem = jasmine.createSpy('MockHangManOptionListItem');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'HangManOptionListItem': MockHangManOptionListItem
        };
        createController = function() {
            $injector.get('$controller')("HangManOptionListItemDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:hangManOptionListItemUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

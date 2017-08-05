'use strict';

describe('TagPool Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockTagPool, MockTag;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockTagPool = jasmine.createSpy('MockTagPool');
        MockTag = jasmine.createSpy('MockTag');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'TagPool': MockTagPool,
            'Tag': MockTag
        };
        createController = function() {
            $injector.get('$controller')("TagPoolDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:tagPoolUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

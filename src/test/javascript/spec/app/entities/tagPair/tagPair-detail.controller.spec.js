'use strict';

describe('TagPair Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockTagPair, MockTag, MockImageCompletionSolution;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockTagPair = jasmine.createSpy('MockTagPair');
        MockTag = jasmine.createSpy('MockTag');
        MockImageCompletionSolution = jasmine.createSpy('MockImageCompletionSolution');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'TagPair': MockTagPair,
            'Tag': MockTag,
            'ImageCompletionSolution': MockImageCompletionSolution
        };
        createController = function() {
            $injector.get('$controller')("TagPairDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:tagPairUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});

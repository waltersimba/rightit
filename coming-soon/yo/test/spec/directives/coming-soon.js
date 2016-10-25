'use strict';

describe('Directive: comingSoon', function () {

  // load the directive's module
  beforeEach(module('comingSoonApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<coming-soon></coming-soon>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the comingSoon directive');
  }));
});

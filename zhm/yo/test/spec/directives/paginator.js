'use strict';

describe('Directive: paginator', function () {

  // load the directive's module
  beforeEach(module('storeApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<paginator></paginator>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the paginator directive');
  }));
});

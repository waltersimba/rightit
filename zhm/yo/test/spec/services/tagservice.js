'use strict';

describe('Service: tagService', function () {

  // load the service's module
  beforeEach(module('storeApp'));

  // instantiate service
  var tagService;
  beforeEach(inject(function (_tagService_) {
    tagService = _tagService_;
  }));

  it('should do something', function () {
    expect(!!tagService).toBe(true);
  });

});

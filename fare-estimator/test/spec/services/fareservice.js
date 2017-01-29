'use strict';

describe('Service: fareService', function () {

  // load the service's module
  beforeEach(module('fareEstimatorApp'));

  // instantiate service
  var fareService;
  beforeEach(inject(function (_fareService_) {
    fareService = _fareService_;
  }));

  it('should do something', function () {
    expect(!!fareService).toBe(true);
  });

});

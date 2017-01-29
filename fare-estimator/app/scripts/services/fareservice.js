'use strict';

/**
 * @ngdoc service
 * @name fareEstimatorApp.fareService
 * @description
 * # fareService
 * Service in the fareEstimatorApp.
 */
angular.module('fareEstimatorApp').service('fareService', function ($http) {

  var api = {};

  api.calculateFare = function (from, to) {
    var fareRequest = {
      from: {
        lat : from.lat,
        lng : from.lng
      },
      to: {
        lat : to.lat,
        lng : to.lng
      }
    };
    return $http.post('/geolocation/api/fare', fareRequest).then(function (response) {
      return response.data;
    });
  };

  return api;
});

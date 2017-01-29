'use strict';

/**
 * @ngdoc function
 * @name fareEstimatorApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the fareEstimatorApp
 */
angular.module('fareEstimatorApp').controller('MainCtrl', function (fareService) {

  var vm = this;

  vm.hasFareEstimate = function () {
    return vm.fareEstimate;
  };

  vm.calculateFare = function () {
    vm.fareEstimate = undefined;
    vm.loading = true;
    fareService.calculateFare(vm.pickupLocation, vm.destination).then(function (response) {
      vm.fareEstimate = response;
    }).finally(function () {
      vm.loading = false;
    });
  };

});

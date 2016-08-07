'use strict';

/**
 * @ngdoc directive
 * @name storeApp.directive:navbar
 * @description
 * # navbar
 */
angular.module('storeApp').directive('navbar', function () {
    return {
      templateUrl: 'views/navbar.html',
      restrict: 'E',
      controllerAs: "vm",
      controller: function ($scope, $location) {
          var vm = this;

          vm.isActive = function(route) {
              return route === $location.path();
          };
      }
    };
  });

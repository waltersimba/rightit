'use strict';

/**
 * @ngdoc overview
 * @name storeApp
 * @description
 * # storeApp
 *
 * Main module of the application.
 */
angular
  .module('storeApp', [
    'ngRoute'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/products', {
        template: '<products></products>'
      })
      .when('/cart', {
        templateUrl: 'views/cart.html',
        controller: 'CartCtrl',
        controllerAs: 'vm'
      })
      .when('/checkout', {
        templateUrl: 'views/checkout.html',
        controller: 'CheckoutCtrl',
        controllerAs: 'vm'
      })
      .otherwise({
        redirectTo: '/products'
      });
  });

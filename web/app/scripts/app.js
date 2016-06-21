'use strict';

/**
 * @ngdoc overview
 * @name taxibookApp
 * @description
 * # taxibookApp
 *
 * Main module of the application.
 */
angular.module('taxibookApp', ['ngRoute']).config(function ($routeProvider) {
  $routeProvider
    .when('/', {
      templateUrl: 'views/main.html',
      controller: 'MainCtrl',
      controllerAs: 'main'
    })
    .when('/signin', {
      templateUrl: 'views/login.html',
      controller: 'LoginCtrl',
      controllerAs: 'login'
    })
    .otherwise({
      redirectTo: '/'
    });
});
;

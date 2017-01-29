'use strict';

/**
 * @ngdoc overview
 * @name fareEstimatorApp
 * @description
 * # fareEstimatorApp
 *
 * Main module of the application.
 */
angular.module('fareEstimatorApp', ['ngRoute', 'vsGoogleAutocomplete', 'angular-button-spinner'])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'vm'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

'use strict';

/**
 * @ngdoc overview
 * @name comingSoonApp
 * @description
 * # comingSoonApp
 *
 * Main module of the application.
 */
angular.module('comingSoonApp', ['config'])
  .run(function ($rootScope, ENV) {
    $rootScope.env = ENV;
  });

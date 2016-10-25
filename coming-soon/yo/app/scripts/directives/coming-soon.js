'use strict';

/**
 * @ngdoc directive
 * @name comingSoonApp.directive:comingSoon
 * @description
 * # comingSoon
 */
angular.module('comingSoonApp').directive('comingSoon', function () {
  return {
    templateUrl: 'views/coming-soon.html',
    restrict: 'E',
    controller: function ($scope, ENV) {
      $scope.env = ENV;
    }
  };
});

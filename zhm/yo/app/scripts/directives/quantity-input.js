'use strict';

/**
 * @ngdoc directive
 * @name storeApp.directive:quantityInput
 * @description
 * # quantityInput
 */
angular.module('storeApp').directive('quantityInput', function () {
    return {
        templateUrl: 'views/quantity-input.html',
        restrict: 'E',
        scope: {
            item: '=ngModel',
            callback: '&'
        },
        controllerAs: 'vm',
        controller: function ($scope) {
            var vm = this;

            vm.increase = function () {
                $scope.item.quantity += 1;
                console.log("Quantity: " + $scope.item.quantity);
                $scope.callback()($scope.item);
            };

            vm.decrease = function () {
                if ($scope.item.quantity > 1) {
                    $scope.item.quantity -= 1;
                    console.log("Quantity: " + $scope.item.quantity);
                    $scope.callback()($scope.item);
                }
            };

        }
    };
});

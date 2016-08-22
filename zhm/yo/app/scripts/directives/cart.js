'use strict';

/**
 * @ngdoc directive
 * @name storeApp.directive:cart
 * @description
 * # cart
 */
angular.module('storeApp').directive('cart', function () {
    return {
        templateUrl: 'views/cart.html',
        restrict: 'E',
        controllerAs: 'vm',
        controller: function ($scope, $rootScope, $log, cartService) {
            var vm = this;

            vm.getTitle = function (item) {
                return item.product.title;
            };

            vm.getAmount = function (amount) {
                return amount ? amount.symbol + amount.total : '';
            };

            vm.isEmptyCart = function () {
                return cartService.isEmpty();
            };

            vm.refreshCart = function () {
                cartService.refreshCart().then(function (response) {
                    $scope.cart = response;
                }, function (error) {
                    $log.error(angular.toJson(error));
                });
            };

            vm.updateQuantity = function (item) {
                cartService.addOrUpdate(item.product._id, item.quantity).then(function (response) {
                    $scope.cart = response;
                }, function (error) {
                    $log.error(angular.toJson(error));
                });
            };

            vm.quantityChanged = function(item) {
                vm.updateQuantity(item);
            };

            vm.removeItem = function (item) {
                cartService.removeItem(item.product._id).then(function (response) {
                    $scope.cart = response;
                }, function (error) {
                    $log.error(angular.toJson(error));
                });
            };

            vm.refreshCart();

            $scope.$on("cart:refresh", function () {
                $log.log("Received cart:refresh event.");
                vm.refreshCart();
            });
        }
    };
});

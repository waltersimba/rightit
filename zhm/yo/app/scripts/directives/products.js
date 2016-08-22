'use strict';

/**
 * @ngdoc directive
 * @name storeApp.directive:products
 * @description
 * # products
 */
angular.module('storeApp').directive('products', function () {
    return {
        templateUrl: 'views/products.html',
        restrict: 'E',
        controllerAs: 'vm',
        controller: function ($scope, $rootScope, $log, $timeout, productService, cartService) {
            var vm = this;
            $scope.products = [];

            vm.hasProducts = function () {
                return !productService.isEmpty();
            };

            vm.getImageLink = function (product) {
                var imageLink = _.findWhere(product.links, {"rel": "photo"});
                return imageLink.href;
            };

            vm.addToCart = function (product) {
                $log.log("Trigger cart:add -> " + product._id);
                $rootScope.$broadcast("cart:add", product);
            };

            vm.addToWishlist = function (product) {
                $log.log("Trigger wishlist:add -> " + product._id);
                $rootScope.$broadcast("wishlist:add", product);
            };

            productService.fetchProducts().then(function (response) {
                $scope.products = response;
            }, function (error) {
                $log.error(error.message);
            });
        }
    };
});

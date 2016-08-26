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
        controller: function ($scope, $rootScope, $log, $timeout, productService) {
            var vm = this;

            vm.defaultOffset = 0;
            vm.defaultLimit = 8;

            $scope.products = [];

            vm.hasProducts = function () {
                return !productService.isEmpty();
            };

            vm.getImageLink = function (product) {
                var imageLink = _.findWhere(product.links, {"rel": "photo"});
                return imageLink ? imageLink.href : "#/products";
            };

            vm.addToCart = function (product) {
                $log.log("Trigger cart:add -> " + product._id);
                $rootScope.$broadcast("cart:add", product);
            };

            vm.addToWishlist = function (product) {
                $log.log("Trigger wishlist:add -> " + product._id);
                $rootScope.$broadcast("wishlist:add", product);
            };

            vm.refreshItems = function (page) {
                vm.refreshProducts((page - 1) * $scope.pagination.items_per_page, $scope.pagination.items_per_page);
            };

            vm.refreshProducts = function (offset, limit) {
                productService.fetchProducts(offset || vm.defaultOffset, limit || vm.defaultLimit).then(function (response) {
                    $scope.items = response.items;
                    $scope.pagination = response.pagination;
                }, function (error) {
                    $log.error(error.message);
                });
            };

            vm.refreshProducts();
        }
    };
});

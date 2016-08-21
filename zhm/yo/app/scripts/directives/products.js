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
        controller: function ($scope, $log, productService) {
            var vm = this;
            $scope.products = [];

            vm.getImageLink = function(product) {
                var imageLink = _.findWhere(product.links, {"rel" : "photo"});
                return imageLink.href;
            };

            productService.fetchProducts().then(function(response){
               $scope.products = response;
            }, function(error) {
                $log.error(error.message);
            });
        }
    };
});

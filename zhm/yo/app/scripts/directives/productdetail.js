'use strict';

/**
 * @ngdoc directive
 * @name storeApp.directive:productDetail
 * @description
 * # productDetail
 */
angular.module('storeApp').directive('productDetail', function () {
    return {
        templateUrl: 'views/product-detail.html',
        restrict: 'E',
        controllerAs: "vm",
        controller: function ($scope, $routeParams,$log, productService) {
            var vm = this;

            vm.productExists = function () {
                return ($scope.product);
            };

            vm.isInStock = function(product) {
                return product.inventory && product.inventory > 0;
            };

            vm.getImageLink = function (product) {
                var imageLink = _.findWhere(product.links, {"rel": "photo"});
                return imageLink.href;
            };

            if(!productService.isEmpty()) {
                $scope.product = productService.findProductById($routeParams.product_id);
            } else {
                productService.fetchProducts().then(function(response) {
                    $scope.product = productService.findProductById($routeParams.product_id, response.items);
                });
            }

        },
        link: function postLink(scope, element, attrs) {
            $(function () {
                if (get_width() >= 768) {
                    $('.image-detail img').ezPlus({
                        responsive: true,
                        respond: [
                            {
                                range: '1200-10000',
                                zoomWindowHeight: 490,
                                zoomWindowWidth: 782
                            },
                            {
                                range: '992-1200',
                                zoomWindowHeight: 400,
                                zoomWindowWidth: 649
                            },
                            {
                                range: '768-992',
                                zoomWindowHeight: 300,
                                zoomWindowWidth: 502
                            },
                            {
                                range: '100-768',
                                zoomWindowHeight: 0,
                                zoomWindowWidth: 0
                            }
                        ]
                    });
                }
            });
        }
    };
});

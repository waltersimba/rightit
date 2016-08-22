'use strict';

/**
 * @ngdoc service
 * @name storeApp.productService
 * @description
 * # productService
 * Factory in the storeApp.
 */
angular.module('storeApp').factory('productService', function ($http) {
    var api = {};

    api.products = [];

    api.isEmpty = function () {
        return api.products.length == 0;
    };

    api.fetchProducts = function () {
        return $http.get('/catalog/api/products').then(function (response) {
            api.products = response.data;
            return api.products;
        });
    };

    return api;
});

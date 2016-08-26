'use strict';

/**
 * @ngdoc service
 * @name storeApp.productService
 * @description
 * # productService
 * Factory in the storeApp.
 */
angular.module('storeApp').factory('productService', function ($http, $log) {
    var api = {};

    api.products = [];

    api.isEmpty = function () {
        return api.products.length == 0;
    };

    api.searchProducts = function (offset, limit, tags) {
        var url = '/catalog/api/products/search?offset=' + offset + "&limit=" + limit;
        if(tags) {
            url += "&tags=" + tags;
        }
        return $http.get(url).then(function (response) {
            api.products = response.data;
            return api.products;
        });
    };

    api.fetchProducts = function (offset, limit) {
        var url = '/catalog/api/products?offset=' + offset + "&limit=" + limit;
        return $http.get(url).then(function (response) {
            api.products = response.data;
            return api.products;
        });
    };

    return api;
});

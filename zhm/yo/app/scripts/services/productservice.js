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
    api.DEFAULT_OFFSET = 0;
    api.DEFAULT_LIMIT = 8;

    api.isEmpty = function () {
        return api.products.length === 0;
    };

    api.searchProducts = function (offset, limit, tags) {
        var url = '/catalog/api/products/search?offset=' + offset + "&limit=" + limit;
        return $http.post(url, {criteria : tags}).then(function (response) {
            api.products = response.data;
            return api.products;
        });
    };

    api.fetchProducts = function (offset, limit) {
        var url = '/catalog/api/products?offset=' + (offset || api.DEFAULT_OFFSET) + "&limit=" + (limit || api.DEFAULT_LIMIT);
        return $http.get(url).then(function (response) {
            api.products = response.data;
            return api.products;
        });
    };

    api.findProductById = function (productId, items) {
        return _.findWhere(items ? items : api.products.items, {"_id": productId});
    };

    return api;
});

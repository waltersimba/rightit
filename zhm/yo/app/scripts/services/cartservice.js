'use strict';

/**
 * @ngdoc service
 * @name storeApp.cartService
 * @description
 * # cartService
 * Factory in the storeApp.
 */
angular.module('storeApp').factory('cartService', function ($http, $log, $rootScope) {
    var api = {};

    api.cart = {};

    api.isEmpty = function () {
        return angular.equals({}, api.cart) || api.cart.items.length == 0;
    };

    api.refreshCart = function () {
        $log.log("Refreshing cart ...");
        return $http.get('/catalog/api/cart').then(function (response) {
            api.cart = response.data || {};
            return api.cart;
        });
    };

    api.addOrUpdate = function (productId, quantity) {
        $log.log("Add/update product = " + productId + ", quantity = " + quantity);
        return $http.post('/catalog/api/cart/' + productId + '?quantity=' + quantity).then(function (response) {
            api.cart = response.data || {};
            return api.cart;
        });
    };

    api.removeItem = function (productId) {
        $log.log("Remove product = " + productId);
        return api.addOrUpdate(productId, 0);
    };

    $rootScope.$on("cart:add", function (event, product) {
        $log.log("Processing cart:add event ...");
        api.addOrUpdate(product._id, 1).then(function (response) {
            $log.log("Trigger cart:refresh");
            $rootScope.$broadcast("cart:refresh");
        });
    });

    return api;
});

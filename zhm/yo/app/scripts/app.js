'use strict';

/**
 * @ngdoc overview
 * @name storeApp
 * @description
 * # storeApp
 *
 * Main module of the application.
 */
angular.module('storeApp', ['ngRoute', 'checklist-model'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/products', {
                template: '<products></products>'
            })
            .when('/products/:product_id', {
                template: '<product-detail></product-detail>'
            })/*
            .when('/cart', {
                template: '<cart></cart>'
            })
            .when('/checkout', {
                templateUrl: 'views/checkout.html',
                controller: 'CheckoutCtrl',
                controllerAs: 'vm'
            })*/
            .otherwise({
                redirectTo: '/products'
            });
    });

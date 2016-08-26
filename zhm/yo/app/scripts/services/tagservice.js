'use strict';

/**
 * @ngdoc service
 * @name storeApp.tagService
 * @description
 * # tagService
 * Factory in the storeApp.
 */
angular.module('storeApp').factory('tagService', function ($http) {
    var api = {};

    api.tags = [];

    api.fetchTags = function () {
        return $http.get("catalog/api/tags").then(function (response) {
            api.tags = response.data;
            return api.tags;
        });
    };

    return api;
});

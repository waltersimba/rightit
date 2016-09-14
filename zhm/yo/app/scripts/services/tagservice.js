'use strict';

/**
 * @ngdoc service
 * @name storeApp.tagService
 * @description
 * # tagService
 * Factory in the storeApp.
 */
angular.module('storeApp').factory('tagService', function ($http, $q) {
    var api = {};

    api.tags = [];

    api.fetchCategoryTags = function () {
        return $http.get("catalog/api/tags/categories").then(function (response) {
            api.tags = response.data;
            return api.tags;
        });
    };

    api.fetchAvailabilityTags = function () {
        return $http.get("catalog/api/tags/availability").then(function (response) {
            api.tags = response.data;
            return api.tags;
        });
    };

    api.fetchTags = function () {
        return $q.all([api.fetchCategoryTags(), api.fetchAvailabilityTags()]).then(function (responses) {
            return {
                "Categories": responses[0],
                "Availability": responses[1]
            }
        });
    };

    return api;
});

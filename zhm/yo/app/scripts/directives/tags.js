'use strict';

/**
 * @ngdoc directive
 * @name storeApp.directive:tags
 * @description
 * # tags
 */
angular.module('storeApp').directive('tags', function () {
    return {
        templateUrl: 'views/tags.html',
        scope: {
            onTagsChanged: "&"
        },
        restrict: 'E',
        controllerAs: "vm",
        controller: function ($scope, $log, tagService) {
            var vm = this;

            vm.tagChanged = function (tags) {
                $scope.onTagsChanged()(tags);
            };

            tagService.fetchTags().then(function (response) {
                $scope.tags = response;
            });
        }
    };
});

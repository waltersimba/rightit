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

            vm.tagMap = {};

            vm.check = function(tagGroup, tag, checked) {
                var key = tagGroup.toUpperCase();
                var tags = vm.tagMap[key];
                if(!tags) {
                    vm.tagMap[key] = [];
                }
                var idx = tags.indexOf(tag);
                if(idx >= 0 && !checked) {
                    tags.splice(idx, 1);
                }
                if(idx < 0 && checked) {
                    tags.push(tag);
                }
                $scope.onTagsChanged()(vm.tagMap);
            };

            vm.getTags = function(tagGroup) {
                var key = tagGroup.toUpperCase();
                if(!vm.tagMap[key]) {
                    vm.tagMap[key] = [];
                }
                return vm.tagMap[tagGroup];
            };

            tagService.fetchTags().then(function (response) {
                $scope.tags = response;
            });
        }
    };
});

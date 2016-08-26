'use strict';

/**
 * @ngdoc directive
 * @name storeApp.directive:paginator
 * @description
 * # paginator
 */
angular.module('storeApp').directive('paginator', function () {
    return {
        templateUrl: 'views/paginator.html',
        scope: {
            pagination: "=",
            onPageChanged: "&"
        },
        restrict: 'E',
        controllerAs: "vm",
        controller: function ($scope, $log) {
            var vm = this;

            vm.setPage = function (page) {
                if (page < 1 || page > $scope.pagination.total_pages) return;
                if (page != $scope.pagination.current_page) {
                    $scope.onPageChanged()(page);
                }
            };

            vm.buildPages = function (totalPages) {
                $scope.pages = [];
                for (var i = 1; i <= totalPages; i++) {
                    var page = {"value": i};
                    $scope.pages.push(page);
                }
            };

            $scope.$watch('pagination.total_pages', function (newValue) {
                if (newValue) {
                    vm.buildPages(newValue);
                }
            });
        }
    };
});

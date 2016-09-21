'use strict';

/**
 * @ngdoc directive
 * @name storeApp.directive:sortingBar
 * @description
 * # sortingBar
 */
angular.module('storeApp').directive('sortingBar', function () {
    return {
        templateUrl: 'views/sorting-bar.html',
        replace: true,
        restrict: 'E',
        controllerAs: 'vm',
        controller: function ($scope) {
            var vm = this;
            $scope.itemsPerPageOptions = [{ value : 8}, { value : 12}, { value: 16}];
            $scope.itemsPerPage = $scope.itemsPerPageOptions[0];
            $scope.sortOptions = [
                {
                    title: "Low Price &raquo; High Price",
                    value: "ASC"
                },
                {
                    title: "High Price &raquo; Low Price",
                    value: "DESC"
                }
            ];
            $scope.sortBy = undefined;

            vm.sortOptionChanged = function(option) {
                if(option !== $scope.sortBy) {
                    //fire an event
                }
            };

            vm.itemsPerPageChanged = function(value) {
                if($scope.itemsPerPage !== value) {
                    //fire an event
                }
            }
        }
    };
});

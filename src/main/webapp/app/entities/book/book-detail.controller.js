(function() {
    'use strict';

    angular
        .module('pickabookApp')
        .controller('BookDetailController', BookDetailController);

    BookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Book', 'Author'];

    function BookDetailController($scope, $rootScope, $stateParams, previousState, entity, Book, Author) {
        var vm = this;

        vm.book = entity;
        // Inject methods to DTOs (FIXME: refactor)
        angular.extend(vm.book.author, {
            getFullName : function() { return this.firstName + " " + this.lastName; }
        });

        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pickabookApp:bookUpdate', function(event, result) {
            vm.book = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

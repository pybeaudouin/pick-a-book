(function() {
    'use strict';

    angular
        .module('pickabookApp')
        .controller('AuthorDetailController', AuthorDetailController);

    AuthorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Author', 'Book'];

    function AuthorDetailController($scope, $rootScope, $stateParams, previousState, entity, Author, Book) {
        var vm = this;

        vm.author = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pickabookApp:authorUpdate', function(event, result) {
            vm.author = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('pickabookApp')
        .controller('BookDialogController', BookDialogController);

    BookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Book', 'Author'];

    function BookDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Book, Author) {
        var vm = this;

        vm.book = entity;
        vm.clear = clear;
        vm.save = save;
        vm.authors = Author.query({filter: 'book-is-null'});
        $q.all([vm.book.$promise, vm.authors.$promise]).then(function() {
            if (!vm.book.authorId) {
                return $q.reject();
            }
            return Author.get({id : vm.book.authorId}).$promise;
        }).then(function(author) {
            vm.authors.push(author);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.book.id !== null) {
                Book.update(vm.book, onSaveSuccess, onSaveError);
            } else {
                Book.save(vm.book, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pickabookApp:bookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

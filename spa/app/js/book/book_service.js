'use strict';

/* Services */

angular.module('userAdmin.bookService', ['ngCookies','http-auth-interceptor'])
  .value('version', '0.1')

    .factory('Books', ['$http', 'REST_URLS', function ($http, REST_URLS) {
        function getBooks(successCallback, errorCallback) {
            $http.post(REST_URLS.BOOKS)
                .success(function (data) {
                    successCallback(data);
                })
                .error(errorCallback);
        }

        function lookUpBookByIsbn(bookIsbn, successCallback, errorCallback) {
            $http.jsonp(REST_URLS.BOOKS_BY_ISBN_GOOGLE + bookIsbn)
                .success(function (data) {
                    successCallback(data);
                })
                .error(errorCallback);
        }

        //TODO: test me pls
        function getBook(bookId, successCallback, errorCallback) {
            $http.get(REST_URLS.BOOK + '/' + bookId)
                .success(function (data) {
                    successCallback(data);
                })
                .error(function (error) {
                    errorCallback(error);
                });
        }

        //TODO: test me pls
        function addBook(book) {
            return $http.post(REST_URLS.BOOK, book);
        }

        //TODO: test me pls
        function borrowBook(book) {
            return $http.post(REST_URLS.BORROW, book.id);
        }

        //TODO: test me pls
        function returnBook(book) {
            return $http.post(REST_URLS.RETURN, book.id);
        }

        return {
            getBooks: getBooks,
            getBook: getBook,
            addBook: addBook,
            borrowBook: borrowBook,
            returnBook: returnBook,
            lookUpBookByIsbn: lookUpBookByIsbn
        };
    }])

  .constant('REST_URLS', {
    BOOKS: 'http://libraryapp.cegeka.com:8080/backend/rest/books',
    BOOK: 'http://libraryapp.cegeka.com:8080/backend/rest/book',
    BORROW: 'http://libraryapp.cegeka.com:8080/backend/rest/borrow',
    RETURN: 'http://libraryapp.cegeka.com:8080/backend/rest/return',
    BOOKS_BY_ISBN_GOOGLE: 'https://www.googleapis.com/books/v1/volumes?callback=JSON_CALLBACK&q=isbn:'
  });


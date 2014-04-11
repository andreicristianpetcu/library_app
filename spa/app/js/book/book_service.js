'use strict';

/* Services */

angular.module('book.bookService', ['ngCookies','http-auth-interceptor'])
  .value('version', '0.1')

    .factory('Books', ['$http', 'REST_URLS', function ($http, BOOK_URLS) {
        function getBooks(successCallback, errorCallback) {
            $http.get(BOOK_URLS.BOOKS)
                .success(function (data) {
                    successCallback(data);
                })
                .error(errorCallback);
        }

        function lookUpBookByIsbn(bookIsbn, successCallback, errorCallback) {
            $http.jsonp(BOOK_URLS.BOOKS_BY_ISBN_GOOGLE + bookIsbn)
                .success(function (data) {
                    successCallback(data);
                })
                .error(errorCallback);
        }

        function getBook(bookId, successCallback, errorCallback) {
            $http.get(BOOK_URLS.BOOK + '/' + bookId)
                .success(function (data) {
                    successCallback(data);
                })
                .error(function (error) {
                    errorCallback(error);
                });
        }

        function addBook(book) {
            return $http.post(BOOK_URLS.BOOK, book);
        }

        function borrowBook(book) {
            return $http.post(BOOK_URLS.BORROW, book.id);
        }

        //TODO: test me pls
        function returnBook(book) {
            return $http.post(BOOK_URLS.RETURN, book.id);
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

  .constant('BOOK_URLS', {
    BOOKS: 'http://libraryapp.cegeka.com:8080/backend/rest/books',
    BOOK: 'http://libraryapp.cegeka.com:8080/backend/rest/book',
    BORROW: 'http://libraryapp.cegeka.com:8080/backend/rest/borrow',
    RETURN: 'http://libraryapp.cegeka.com:8080/backend/rest/return',
    BOOKS_BY_ISBN_GOOGLE: 'https://www.googleapis.com/books/v1/volumes?callback=JSON_CALLBACK&q=isbn:'
  })

;


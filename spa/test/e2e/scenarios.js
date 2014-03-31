'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('ginkgo admin app', function () {

    beforeEach(function () {
        browser().navigateTo('/app/');
    });

    it('should automatically redirect to / when location hash/fragment is empty', function () {
        expect(browser().location().url()).toBe("/");
    });

    describe('home', function () {
        beforeEach(function () {
            browser().navigateTo('#/');
        });

        it('should render home when user navigates to /', function () {
            expect(element('[ng-view] h1:first').text()).toMatch(/Welcome/);
        });

        it('redirects to books after login', function () {
            input('username').enter('admin@mailinator.com');
            input('password').enter('test');
            element("#login").click();
            expect(browser().location().url()).toBe("/books");
        });

    });

    describe('users', function () {
        beforeEach(function () {
            browser().navigateTo('#/users');
        });

        it('should render users when user navigates to /users', function () {
            expect(element('[ng-view] h1:first').text()).toMatch(/Users/);
        });

        it('should contain a table', function () {
            expect(element('[ng-view] table th').text()).toMatch("#NameEmailRoleEdit");
        });

    });

    describe('books view', function () {

        beforeEach(function () {
            browser().navigateTo('#/books');
            //mock some books?
        });

        it('should render books when user navigates to /books', function () {
            expect(element('[ng-view] h1:first').text()).toMatch(/Books/);
        });

        it('should contain a list of books', function () {
            expect(element('[ng-view] table th').text()).toMatch("TitleAuthorISBN");
        });

        it('should contain an add book button when user has admin role', function () {
            expect(element('[ng-view] #btnAddBook').text()).toMatch("add book");
        });

        it('should not contain an add book button when user does not have admin role', function () {
//            TODO implement
        });

    });

    describe('add book view', function () {

        beforeEach(function () {
            browser().navigateTo('#/add_book');
        });

        it('should display a form', function () {
            //TODO
        });

        it('should post to /book on submit button click', function () {
            //TODO
        });
    });

});

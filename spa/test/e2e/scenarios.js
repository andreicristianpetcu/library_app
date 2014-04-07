'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('Library app', function () {

    beforeEach(function () {
        browser().navigateTo('/app/');
    });

    it('redirects to / when location hash/fragment is empty', function () {
        expect(browser().location().url()).toBe("/");
    });

    describe('home page', function () {

        beforeEach(function () {
            browser().navigateTo('#/');
            if(element('#logout')) {
                element('#logout').click();
                console.log("Logged out!")
            }
        });

        it('renders "Welcome" and login form', function () {
            expect(element('[ng-view] h1:first').text()).toMatch(/Welcome/);
            expect(element('#username').text()).not().toBe(null);
            expect(element('#password').text()).not().toBe(null);
            expect(element('#login').text()).toBe('Login');
            expect(element('#login:visible').count()).toBe(1);
            expect(element('#logout').text()).toBe('Logout');
            expect(element('#logout:visible').count()).toBe(0);
        });

        it('redirects to /books after successful login', function () {
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

        it('should contain a table with Title Author ISBN', function () {
            expect(element('[ng-view] table th').text()).toContain("Title");
            expect(element('[ng-view] table th').text()).toContain("Author");
            expect(element('[ng-view] table th').text()).toContain("ISBN");
        });

        it('should contain an add book button when user has admin role', function () {
            expect(element('[ng-view] #btnAddBook').text()).toMatch("add book");
        });

        it('should not contain an add book button when user does not have admin role', function () {
//            TODO implement
        });

    });

    describe('Add book view', function () {

        beforeEach(function () {
            browser().navigateTo('#/add_book');
        });

        it('should display a form with title, author and isbn inputs and cancel/addbook buttons', function () {
            expect(element('#addBookForm').text()).not().toBeNull();
            expect(element('#inputTitle').text()).not().toBeNull();
            expect(element('#inputAuthor').text()).not().toBeNull();
            expect(element('#inputIsbn').text()).not().toBeNull();
            expect(element('#addBookSubmit').text()).toEqual("Save");
            expect(element('#addBookCancel').text()).toEqual("Cancel");
        });

        it('should allow adding a book that is available and can be borrowed', function () {
            var newIsbn = Math.random() * 1234567890|0;
            input('book.title').enter('testTitle');
            input('book.author').enter('testAuthor');
            input('book.isbn').enter(newIsbn);
            element("#addBookSubmit").click();
            expect(browser().location().url()).toBe("/books");
            expect(element('[ng-view] table tr:last').text()).toContain(newIsbn);
            expect(element('[ng-view] table tr:last').text()).toContain('testTitle');
            expect(element('[ng-view] table tr:last').text()).toContain('testAuthor');
            expect(element('[ng-view] table tr:last').text()).toContain('Available');
            expect(element('[ng-view] table tr:last').text()).toContain('Borrow');
            console.log("poc");
        });

    });

});

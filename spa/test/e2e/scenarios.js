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

    });

    describe('users', function () {

        beforeEach(function () {
            browser().navigateTo('#/users');
        });

        it('should render users when user navigates to /users', function () {
            expect(element('[ng-view] h1:first').text()).toMatch(/Users/);
        });

    });

    describe('user', function () {

        beforeEach(function () {
            browser().navigateTo('#/user/1');
        });

        it('should render user when user navigates to /user/:id', function () {
            console.log(element('body').text);
            expect(element('[ng-view] h1:first').text()).toMatch(/Romeo/);
        });

    });

    describe('books view', function () {

        beforeEach(function () {
            browser().navigateTo('#/books');
            //mock some books?
        });

        it('should render books when user navigates to /user/:id', function () {
            console.log(element('body').text);
            expect(element('[ng-view] h1:first').text()).toMatch(/Books/);
        });

        it('should contain a list of books', function () {
//            TODO implement
        });

        it('should contain an add book button when user has admin role', function () {
//            TODO implement
        });

        it('should not contain an add book button when user does not have admin role', function () {
//            TODO implement
        });

        it('should filter the book list as user types into the search box', function () {
            expect(repeater('.books li').count()).toBe(3);

            input('query').enter('nexus');
            expect(repeater('.books li').count()).toBe(1);

            input('query').enter('motorola');
            expect(repeater('.books li').count()).toBe(2);
        });
    });

    describe('add book view', function () {

        beforeEach(function () {
            browser().navigateTo('#/book/1');
        });

        it('should display a form', function () {
            //TODO
        });

        it('should post to /book on submit button click', function () {
            //TODO
        });
    });

    describe('book view', function () {

        beforeEach(function () {
            browser().navigateTo('#/book/1');
        });

        it('should render book when user navigates to /user/:id', function () {
            console.log(element('body').text);
            expect(element('[ng-view] h1:first').text()).toMatch(/Book Title/);
        });

    });
});

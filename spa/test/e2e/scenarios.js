'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('ginkgo admin app', function () {

    beforeEach(function () {
        browser().navigateTo('/app/index.html');
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

    describe('books', function () {


        beforeEach(function () {
            browser().navigateTo('#/books');
        });


        it('should filter the book list as user types into the search box', function () {
            expect(repeater('.books li').count()).toBe(3);

            input('query').enter('nexus');
            expect(repeater('.books li').count()).toBe(1);

            input('query').enter('motorola');
            expect(repeater('.books li').count()).toBe(2);
        });

    });
});

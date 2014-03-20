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
            expect(element('[ng-view] h1:first').text()).toMatch(/Romeo/);
        });

    });
});

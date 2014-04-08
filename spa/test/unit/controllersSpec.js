'use strict';

describe('controllers', function () {
  var scope;
  beforeEach(function () {
    module('userAdmin.controllers');
  });

  describe('UsersController', function () {
    var alertsMock, usersMock, UsersController;

    beforeEach(function () {
      module(function ($provide) {
        $provide.value('Users', {});
      });

      inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        usersMock = jasmine.createSpyObj('Users', ['getUsers']);
        alertsMock = jasmine.createSpyObj('Alerts',['handler']);

        UsersController = $controller('UsersController', {$scope: scope, Users: usersMock, Alerts: alertsMock});
      });

    });

    it('should get users from User factory', function () {
      expect(usersMock.getUsers).toHaveBeenCalled();
    });

    it('success callback should put users on scope', function () {
      var success = usersMock.getUsers.mostRecentCall.args[0];
      var users = [
        {id: 1, name: 'name'}
      ];

      success(users);

      expect(scope.users).toBe(users);
    });

  });



    describe('BooksController', function () {
        var alertsMock, booksMock, authMock, BooksController;

        beforeEach(function () {
            module(function ($provide) {
                $provide.value('Books', {});
            });

            inject(function ($controller, $rootScope) {
                scope = $rootScope.$new();
                booksMock = jasmine.createSpyObj('Books', ['getBooks', 'addBook', 'borrowBook']);
                alertsMock = jasmine.createSpyObj('Alerts',['handler']);
                authMock = jasmine.createSpyObj('Auth',['handler']);

                BooksController = $controller('BooksController', {$scope: scope, Books: booksMock, Alerts: alertsMock, Auth: authMock});
            });

        });

        it('should get books from Books factory', function () {
            expect(booksMock.getBooks).toHaveBeenCalled();
        });

//        it('should call addBooks from Books factory', function () {
//            scope.addBook({});
//            expect(booksMock.addBook).toHaveBeenCalled();
//        });
//
//        it('should call borrowBooks from Books factory', function () {
//            scope.borrowBook({});
//            expect(booksMock.borrowBook).toHaveBeenCalled();
//        });

        it('should simulate promise', inject(function($q, $rootScope) {
            var deferred = $q.defer();
            var promise = deferred.promise;
            var resolvedValue;

            promise.then(function(value) { resolvedValue = value; });
            expect(resolvedValue).toBeUndefined();

            // Simulate resolving of promise
            deferred.resolve(123);
            // Note that the 'then' function does not get called synchronously.
            // This is because we want the promise API to always be async, whether or not
            // it got called synchronously or asynchronously.
            expect(resolvedValue).toBeUndefined();

            // Propagate promise resolution to 'then' functions using $apply().
            $rootScope.$apply();
            expect(resolvedValue).toEqual(123);
        }));

    });


  describe('LoginDirectiveController', function () {
    var url = 'scope-url-value';

    beforeEach(function () {
      module(function ($provide) {
        //jasmine.createSpyObj('Auth', ['authenticate', 'getAuthenticatedUser', 'isAuthorizedToAccess', 'isAuthenticated'])
        $provide.factory('Auth', function () {
          var credentials = {};
          var succesfulLogin = true;

          function authenticate(credentials, successCallback, errorCallback) {
            this.credentials = credentials;
            if (credentials.username != '') {
              successCallback();
            } else {
              errorCallback('ERROR-MESSAGE-FROM-REST');
            }
          }

          function logoutShouldFail() {
            succesfulLogin = false;
          }

          function logout(successCallback, errorCallback) {
            if(succesfulLogin) {
              successCallback();
            } else {
              errorCallback('ERROR-MESSAGE-FROM-REST');
            }
          }

          return {
            authenticate: authenticate,
            credentials: credentials,
            logout: logout,
            logoutShouldFail: logoutShouldFail
          }
        });
      });

      inject(function ($controller, $rootScope) {
        $rootScope.alerts = [];
        scope = $rootScope.$new();
        scope.url = url;
        scope.afterLogin = function () {
        };
        scope.username = 'username';
        scope.password = 'password';
        $controller('LoginDirectiveController', {$scope: scope});
      });
    });

    it('should delegate authentication to Auth', inject(function (Auth) {
      scope.login();
      expect(Auth.credentials).toEqual({username: scope.username, password: scope.password});
    }));

    it('should call afterLogin method given a successful login', inject(function (Auth) {
      spyOn(scope, 'afterLogin');

      scope.login();

      expect(Auth.credentials).toEqual({username: scope.username, password: scope.password});
      expect(scope.afterLogin).toHaveBeenCalled();
    }));

    it('should add error message on scope given a failed login', inject(function () {
      var responseErrorMessage = 'ERROR-MESSAGE-FROM-REST';
      scope.username = '';

      scope.login();

      expect(scope.alerts).toContain({ type: 'danger', msg: 'Login failed: ' + responseErrorMessage });
    }));

    it('should redirect to root path when logout was successful', inject(function (Auth, $location) {
      scope.logout();

      expect($location.path()).toBe('/');
    }));

    it('should display error message on failed logout', inject(function (Auth) {
      Auth.logoutShouldFail();
      scope.logout();

      expect(scope.alerts).toContain({ type: 'danger', msg: 'There was an error: ERROR-MESSAGE-FROM-REST' });
    }));
  });

});

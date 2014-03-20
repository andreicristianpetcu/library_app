'use strict';

/* Controllers */

angular.module('userAdmin.controllers', [])
  .controller('HomeCtrl', ['$scope', function ($scope) {
  }])
  .controller('LoginController', ['$scope', '$location', LoginController])
  .controller('LoginDirectiveController', ['$rootScope', '$scope', 'Auth', '$location', LoginDirectiveController])
  .controller('UsersController', ['$rootScope', 'Users', '$scope','$location','Alerts', UsersController])
  .controller('UserCtrl', [function () {
  }]);


function LoginController($scope, $location) {
  $scope.redirectTo = function (route) {
    $location.path(route);
  }
}

function LoginDirectiveController($rootScope, $scope, Auth, $location) {
  $scope.login = function () {
    var credentials = {username: $scope.username, password: $scope.password};
    Auth.authenticate(credentials, function () {
      $scope.afterLogin();
    }, function (error) {
      $rootScope.alerts.push({ type: 'danger', msg: 'Login failed: ' + error });
    });
  };

  $scope.isAuthenticated = function () {
    return Auth.isAuthenticated();
  }

  $scope.logout = function () {
    Auth.logout(function() {
      $location.path('/');
    },
    function (error) {
      $rootScope.alerts.push({ type: 'danger', msg: 'There was an error: ' + error });
    });
  }
}

function UsersController($rootScope, Users, $scope, $location, Alerts) {
    Users.getUsers(
        function success(responseData) {
            $scope.users = responseData;
        },
        Alerts.handler
    );


  var MENU_ACTION_IDS = {
    ACTION_1: 'ACTION_1'
  }

  $scope.sidebarMenuItems = [
    {id: MENU_ACTION_IDS.ACTION_1, text: 'Action 1' }
  ];


  $scope.sidebarActionSelected = function (sidebarAction) {
    console.log(sidebarAction);
    alert(sidebarAction.text + " was clicked");
  }
}


function UserController(Users, $scope, $routeParams, $location) {

  $scope.rolesModel = {'ADMIN': false, 'USER': false};
  function updateRolesViewModelFromUser(model, user) {
    user.roles.forEach(function (role) {
      model[role] = true;
    });
  }

  function updateUserFromRolesViewModel(user, model) {
    var roles = [];
    _.keys(model).forEach(function (role) {
      if (model[role] === true) {
        roles.push(role);
      }
    });
    user.roles = roles;

  }

  Users.getUser($routeParams.id,
    function success(responseData) {
      $scope.user = responseData;
      updateRolesViewModelFromUser($scope.rolesModel, $scope.user);
    },
    function error(error) {
      var x = 0;
      // for now do nothing. feel free to add here error messages on scope if you want/need to
    });

  $scope.updateUser = function () {
    updateUserFromRolesViewModel($scope.user, $scope.rolesModel);
    Users.updateUser($scope.user)
      .then(function () {
        $location.path('/users');

      }, function error() {

      })
  }
}



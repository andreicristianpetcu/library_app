'use strict';

/* Services */

angular.module('userAdmin.services', ['ngCookies', 'http-auth-interceptor'])
    .value('version', '0.1')

    .factory('Auth', ['$http', 'REST_URLS', '$cookieStore', function ($http, REST_URLS, $cookieStore) {
        var user = {userId: '', roles: []};

        if ($cookieStore.get('user') !== undefined) {
            angular.extend(user, $cookieStore.get('user'));
        }

        function userHasRole(user, role) {
            return _.indexOf(user.roles, role) !== -1;
        }

        function routeDoesNotRequireAuthentication(route) {
            return route.role === undefined;
        }

        function isAuthorizedToAccess(route) {
            return  routeDoesNotRequireAuthentication(route) || userHasRole(user, route.role);
        }

        function isAuthenticated() {
            return !_.isEmpty(user.userId.trim());
        }

        function authenticate(credentials, successCallback, errorCallback) {
            $http.post(REST_URLS.LOGIN, credentials,
                {
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                    transformRequest: function (data) {
                        return $.param(data);
                    },
                    ignoreAuthModule: true
                })
                .success(function (response) {
                    angular.copy(response, user);
                    $cookieStore.put('user', user);
                    successCallback();
                }).error(function (error) {
                    errorCallback(error);
                });

        }

        function getAuthenticatedUser() {
            var copiedUser = {};
            angular.copy(user, copiedUser);
            return copiedUser;
        }

        function logout(successCallback, errorCallback) {
            $cookieStore.remove('user');
            user.userId = '';
            user.roles = [];
            $http.post(REST_URLS.LOGOUT, {}, {ignoreAuthModule: true}).success(
                function () {
                    if (!_.isUndefined(successCallback)) {
                        successCallback();
                    }
                }).error(function (error) {
                    if (!_.isUndefined(errorCallback)) {
                        errorCallback(error);
                    }
                });
        }

        return {
            authenticate: authenticate,
            getAuthenticatedUser: getAuthenticatedUser,
            isAuthorizedToAccess: isAuthorizedToAccess,
            isAuthenticated: isAuthenticated,
            logout: logout
        };
    }])

    .factory('Alerts', ['$rootScope', function ($rootScope) {
        function genericErrorHandler(response) {
            var status = response.status;
            var headers = response.headers;
            var config = response.config;
            var data = response.data;

            var errorMessage;
            if (status === 0) {
                errorMessage = 'Timeout while accessing ' + config.url;
            } else {
                errorMessage = 'Accessing ' + config.url + ' returned with status code: ' + status + " \r\n" + data;
            }
            $rootScope.alerts.push({msg: errorMessage, type: "danger" });
        }

        function successHandler(message) {
            $rootScope.alerts.push({msg: message, type: "success" });
        }

        return {
            handler: genericErrorHandler,
            successHandler: successHandler
        }
    }])

    .constant('REST_URLS', {
        LOGIN: 'http://libraryapp.cegeka.com:8080/backend/j_spring_security_check',
        LOGOUT: 'http://libraryapp.cegeka.com:8080/backend/j_spring_security_logout',
    });


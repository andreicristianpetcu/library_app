'use strict';

angular.module('userAdmin.directives', [])
    .directive('appVersion', ['version', function (version) {
        return function (scope, elm, attrs) {
            elm.text(version);
        };
    }])

    .directive('sidebarActions', function () {
        return {
            restrict: 'E',
            templateUrl: 'directive/sidebar-actions.html',
            scope: {
                title: "=",
                items: "=",
                action: "&onAction"
            },

            link: function (scope, directiveElement, attrs) {
                scope.title = attrs.title;

                scope.isActive = function (which) {
                    return which === activeMenuItem;
                }

                scope.selectMenuItem = function (index) {
                    scope.action({action: scope.items[index]});
                }
            }
        }
    });

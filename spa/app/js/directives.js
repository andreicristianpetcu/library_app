'use strict';
var app = angular.module('userAdmin.directives', []);

app.directive('appVersion', ['version', function (version) {
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

app.directive("clickToEdit", function() {
    var editorTemplate = '<div class="click-to-edit">' +
        '<div ng-hide="view.editorEnabled">' +
        '{{value}} ' +
        '<a ng-click="enableEditor()">Edit</a>' +
        '</div>' +
        '<div ng-show="view.editorEnabled">' +
        '<input ng-model="view.editableValue">' +
        '<a ng-click="save()">Save</a>' +
        ' or ' +
        '<a ng-click="disableEditor()">cancel</a>.' +
        '</div>' +
        '</div>';

    return {
        restrict: "A",
        replace: true,
        template: editorTemplate,
        scope: {
            value: "=clickToEdit"
        },
        controller: function($scope) {
            $scope.view = {
                editableValue: $scope.value,
                editorEnabled: false
            };

            $scope.enableEditor = function() {
                $scope.view.editorEnabled = true;
                $scope.view.editableValue = $scope.value;
            };

            $scope.disableEditor = function() {
                $scope.view.editorEnabled = false;
            };

            $scope.save = function() {
                $scope.value = $scope.view.editableValue;
                $scope.disableEditor();
            };
        }
    };
});

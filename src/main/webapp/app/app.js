var app = angular.module('appMarvelApi', ['ngRoute','ngResource','ngCookies']);

app.config(function ($routeProvider) {

    $routeProvider.when("/", {
        controller: "",
        templateUrl: "app/views/home.html"
    });

    $routeProvider.when("/user/home", {
        controller: "",
        templateUrl: "app/views/user/home.html"
    });

    $routeProvider.when("/register", {
        controller: "signupController",
        templateUrl: "app/views/singup.html"
    });

    $routeProvider.when("/favorites", {
        controller: "favoritesController",
        templateUrl: "app/views/user/favorites.html"
    });

    //$routeProvider.otherwise({ redirectTo: "/" });
});

app.controller('signupController', ['$scope', '$location', function($scope, $location) {
    // TODO: Agregar lógica del controlador.
}]);
var app = angular.module('appMarvelApi', ['ngRoute','ngResource']);

app.config(function ($routeProvider) {

    $routeProvider.when("/", {
        controller: "",
        templateUrl: "app/views/home.html"
    });

    $routeProvider.when("/register", {
        controller: "registerController",
        templateUrl: "app/views/register.html"
    });

});

checkUser();
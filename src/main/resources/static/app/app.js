var app = angular.module('appMarvelApi', ['ngRoute','ngResource']);

app.config(function ($routeProvider) {

    $routeProvider.when("/", {
        controller: "",
        templateUrl: "app/views/home.html"
    });

    $routeProvider.when("/register", {
        controller: "singupController",
        templateUrl: "app/views/singup.html"
    });

});

checkUser();
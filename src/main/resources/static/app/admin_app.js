/**
 * Created by ivan on 31/05/16.
 */
var app = angular.module('appAdmin', ['ngRoute', 'ngResource']);

app.config(function ($routeProvider, cfpLoadingBarProvider) {

    $routeProvider.when("/", {
        controller: "",
        templateUrl: "app/views/user/home.html"
    });

    $routeProvider.when("/users/:id", {
        controller: "userDataController",
        templateUrl: "app/views/admin/user.html"
    });

    $routeProvider.when("/characters/ranking", {
        controller: "rankingController",
        templateUrl: "app/views/admin/ranking.html"
    });

    $routeProvider.when("/teams/commons/:id_1/:id_2", {
        controller: "commonsController",
        templateUrl: "app/views/admin/intersection.html"
    });

});
/**
 * Created by ivan on 31/05/16.
 */
var app = angular.module('appAdmin', ['ngRoute', 'ngResource']);
app.filter('range', function () {
    return function (input, total) {
        total = parseInt(total);
        for (var i = 0; i < total; i++)
            input.push(i);
        return input;
    };
});

app.config(function ($routeProvider) {

    $routeProvider.when("/", {
        controller: "",
        templateUrl: "app/views/admin/home.html"
    });

    $routeProvider.when("/users", {
        controller: "userController",
        templateUrl: "app/views/admin/users.html"
    });

    $routeProvider.when("/users/:id", {
        controller: "userController",
        templateUrl: "app/views/admin/user.html"
    });

    $routeProvider.when("/ranking", {
        controller: "rankingController",
        templateUrl: "app/views/admin/ranking.html"
    });

    $routeProvider.when("/teams/commons/:id_1/:id_2", {
        controller: "commonsController",
        templateUrl: "app/views/admin/intersection.html"
    });

});
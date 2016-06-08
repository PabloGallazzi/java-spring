/**
 * Created by ivan on 11/05/16.
 */
'use strict';
app.controller('favoritesController',
    ['$scope',
        '$location',
        '$route',
        'favoritesService',
        'characterService',
        function ($scope,
                  $location,
                  $route,
                  favoritesService,
                  characterService) {

            var favoritesController = this;

            favoritesController.reloadPage = reloadPage;
            favoritesController.addFavorite = addFavorite;
            favoritesController.removeFavorite = removeFavorite;
            favoritesController.isFavorite = isFavorite;

            $scope.selectFavorite = selectFavorite;
            $scope.getFavorites = getFavorites;

            $scope.favorites = [];

            // Functions

            function reloadPage() {
                $route.reload();
            }

            function selectFavorite(character) {
                characterService.setSelectedCharacter(character);
            }

            function addFavorite(character) {
                favoritesService.addFavorite(character).then(reloadPage);
            }

            function removeFavorite(character) {
                favoritesService.removeFavorite(character).then(reloadPage);
            }

            function isFavorite(character) {
                var condition = function (char) {
                    return char.id == character.id
                };
                return $scope.favorites.some(condition);
            }

            function getFavorites() {
                favoritesService.getFavorites().then(function (favorites) {
                    $scope.favorites = favorites.map(function (fav) {
                        fav.image = fav.thumbnail.path + "." + fav.thumbnail.extension;
                        return fav;
                    });
                })
            }

        }]);
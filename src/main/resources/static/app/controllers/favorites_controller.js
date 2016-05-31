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

            favoritesController.getImagePathFor = getImagePathFor;
            favoritesController.getUserIdBy = getUserIdBy;
            favoritesController.reloadPage = reloadPage;
            favoritesController.addFavorite = addFavorite;
            favoritesController.removeFavorite = removeFavorite;
            favoritesController.isFavorite = isFavorite;

            $scope.selectFavorite = selectFavorite;
            $scope.getFavorites = getFavorites;

            $scope.favorites = [];
            $scope.token = getCookie('access_token');
            $scope.user_id = getUserIdBy($scope.token);

            // Functions

            function getImagePathFor(character) {
                return character.thumbnail.path + '.' + character.thumbnail.extension;
            }

            function getUserIdBy(token) {
                return token.slice(0, token.indexOf('-'));
            }

            function reloadPage() {
                $route.reload();
            }

            function selectFavorite(character) {
                characterService.setSelectedCharacter(character);
            }

            function addFavorite(character) {
                favoritesService.addFavorite($scope.user_id, $scope.token, character).success(reloadPage);
            }

            function removeFavorite(character) {
                favoritesService.removeFavorite($scope.user_id, $scope.token, character).success(reloadPage);
            }

            function isFavorite(character) {
                var condition = function (char) {
                    return char.id == character.id
                };
                return $scope.favorites.some(condition);
            }

            function getFavorites() {
                favoritesService.getFavoritesByToken($scope.user_id, $scope.token).then(function (favorites) {
                    $scope.favorites = favorites.map(function (char) {
                        char.image_path = getImagePathFor(char);
                        return char;
                    });
                })
            }

        }]);
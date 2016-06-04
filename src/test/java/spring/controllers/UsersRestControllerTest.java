package spring.controllers;

import domain.*;
import domain.Character;
import org.junit.Test;
import spring.BaseRestTester;

import java.util.*;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by niko118 on 4/11/16.
 * Modified by pgallazzi on 4/21/16.
 */

public class UsersRestControllerTest extends BaseRestTester {

    @Test
    public void testGetCharactersIntersectionTokenMustBeProvided() throws Exception {
        mockMvc.perform(get("/teams/commons/" + "id123" + "/" + "id123"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersIntersectionBadIds() throws Exception {
        mockMvc.perform(get("/teams/commons/" + "id123" + "/" + "id123" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid id id123")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersIntersectionId1NotFound() throws Exception {
        mockMvc.perform(get("/teams/commons/" + "123456789012345678901234" + "/" + "432109876543210987654321" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Team with id " + "123456789012345678901234" + " was not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersIntersectionId2NotFound() throws Exception {
        createTACSTestTeamWithMember();
        mockMvc.perform(get("/teams/commons/" + "432109876543210987654321" + "/" + "123456789012345678901234" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Team with id " + "432109876543210987654321" + " was not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersIntersectionNoToken() throws Exception {
        mockMvc.perform(get("/teams/commons/1/2?access_token=1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersIntersectionNotFresh() throws Exception {
        Token token = createAndLogInTACSTestUserWithNotFreshToken();
        mockMvc.perform(get("/teams/commons/1/2?access_token=" + token.getAccessToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersIntersectionNotAdmin() throws Exception {
        mockMvc.perform(get("/teams/commons/1/2?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersIntersectionNull() throws Exception {
        Team team1 = new Team();
        team1.setTeamName("uno");
        team1.addMember(getCharactersPool().get(0));
        team1.addMember(getCharactersPool().get(1));
        team1.addMember(getCharactersPool().get(2));
        Team team2 = new Team();
        team2.setTeamName("dos");
        team2.addMember(getCharactersPool().get(3));
        team2.addMember(getCharactersPool().get(4));
        team1 = teamsRepository.save(team1);
        team2 = teamsRepository.save(team2);
        mockMvc.perform(get("/teams/commons/" + team1.getTeamId().toString() + "/" + team2.getTeamId().toString() + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetCharactersIntersectionNotNull() throws Exception {
        Team team1 = new Team();
        team1.setTeamName("uno");
        team1.addMember(getCharactersPool().get(0));
        team1.addMember(getCharactersPool().get(1));
        team1.addMember(getCharactersPool().get(2));
        Team team2 = new Team();
        team2.setTeamName("dos");
        team1 = teamsRepository.save(team1);
        team2.addMember(team1.getMembers().get(1));
        team2.addMember(team1.getMembers().get(2));
        team2 = teamsRepository.save(team2);
        mockMvc.perform(get("/teams/commons/" + team1.getTeamId().toString() + "/" + team2.getTeamId().toString() + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1011335)))
                .andExpect(jsonPath("$[1].id", is(1011336)));
    }

    @Test
    public void testCreateUserSuccessCheckAdminNotSet() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "12345678;");
        request.put("is_admin", true);
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is("TACS")));
        User userCreated = ds.getDatastore().find(User.class, "userName", "TACS").get();
        assertFalse(userCreated.isAdmin());
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "12345678;");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is("TACS")));
    }

    @Test
    public void testCreateUserFailNoPassword() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "userTest");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to create user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation error")))
                .andExpect(jsonPath("$.cause", is(Collections.singletonList("must_provide_a_password"))));
    }

    @Test
    public void testCreateUserFailNoUserName() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_password", "12345678;");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to create user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation error")))
                .andExpect(jsonPath("$.cause", is(Collections.singletonList("must_provide_a_user_name"))));
    }

    @Test
    public void testCreateUserFailBadPasswordNoSpecialCharacters() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "userTest");
        request.put("user_password", "12345678");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to create user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation error")))
                .andExpect(jsonPath("$.cause", is(Collections.singletonList("user_password_must_contain_one_of_|;,._-|"))));
    }

    @Test
    public void testCreateUserFailBadPasswordBelow6Characters() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "userTest");
        request.put("user_password", "1234;");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to create user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation error")))
                .andExpect(jsonPath("$.cause", is(Collections.singletonList("user_password_length_below_6_chars"))));
    }

    @Test
    public void testCreateUserFailBadPasswordBelow6CharactersAndNoSpecialCharacters() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "userTest");
        request.put("user_password", "1234");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to create user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation error")))
                .andExpect(jsonPath("$.cause", is(Arrays.asList("user_password_length_below_6_chars", "user_password_must_contain_one_of_|;,._-|"))));
    }

    @Test
    public void testDuplicatedUser() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "12345678;");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is("TACS")));
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to create user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation error")))
                .andExpect(jsonPath("$.cause", is(Collections.singletonList("user_name_already_used"))));
    }

    @Test
    public void testUnableToGetUserTokenMustBeProvided() throws Exception {
        mockMvc.perform(get("/users/123"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testUnableToGetUserIdLessThan24CharactersReturnsNotFound() throws Exception {
        mockMvc.perform(get("/users/123" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to find resource")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testUnableToGetUserRetrievesNotFound() throws Exception {
        mockMvc.perform(get("/users/123456789012345678900000" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to find resource")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetUserSuccessFull() throws Exception {
        mockMvc.perform(get("/users/123456789012345678901234?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is("TACS")))
                .andExpect(jsonPath("$.admin", is(true)));
    }

    @Test
    public void testGetUserNotAdmin() throws Exception {
        mockMvc.perform(get("/users/123456789012345678901234?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSingleFavoriteTokenMustBeProvided() throws Exception {
        mockMvc.perform(get("/users/123/characters/favorites/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSingleFavoriteMismatchToken() throws Exception {
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        mockMvc.perform(get("/users/" + id2 + "/characters/favorites/1?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSingleFavoriteNotFoundToken() throws Exception {
        mockMvc.perform(get("/users/123456789012345678901234/characters/favorites/1?access_token=1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSingleFavoriteNotFreshToken() throws Exception {
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        token = setNotFreshExpirationDateToToken(token);
        mockMvc.perform(get("/users/" + getTACDId() + "/characters/favorites/1?access_token=" + token.getAccessToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSingleFavoriteIdNotANumber() throws Exception {
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        mockMvc.perform(get("/users/" + getTACDId() + "/characters/favorites/notANumber?access_token=" + token.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Character id must be a positive number")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSingleFavoriteIdNotAPositiveNumber() throws Exception {
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        mockMvc.perform(get("/users/" + getTACDId() + "/characters/favorites/-1?access_token=" + token.getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Character id must be a positive number")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSingleFavoriteIdNotFoundAsFavorite() throws Exception {
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        mockMvc.perform(get("/users/" + getTACDId() + "/characters/favorites/1011335?access_token=" + token.getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("That character is not a favorite of this user")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("character_not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSingleFavoriteOk() throws Exception {
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        mockMvc.perform(get("/users/" + getTACDId() + "/characters/favorites/1011334?access_token=" + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(1011334)))
                .andExpect(jsonPath("$.name", is("3-D Man")));
    }

    @Test
    public void testGetFavoritesTokenMustBeProvided() throws Exception {
        mockMvc.perform(get("/users/123/characters/favorites"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetFavoritesMismatchToken() throws Exception {
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        mockMvc.perform(get("/users/" + id2 + "/characters/favorites?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetFavoritesNotFoundToken() throws Exception {
        mockMvc.perform(get("/users/123456789012345678901234/characters/favorites?access_token=1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetFavoritesNotFreshToken() throws Exception {
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        token = setNotFreshExpirationDateToToken(token);
        mockMvc.perform(get("/users/" + getTACDId() + "/characters/favorites?access_token=" + token.getAccessToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetFavoritesOk() throws Exception {
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        mockMvc.perform(get("/users/" + getTACDId() + "/characters/favorites?access_token=" + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1011334)));
    }

    @Test
    public void testPostFavoritesTokenMustBeProvided() throws Exception {
        String body = json(getTACSTestCharacterVO());
        mockMvc.perform(post("/users/123/characters/favorites")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostFavoritesMismatchToken() throws Exception {
        Token token = createAndLogInTACSTestUser();
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        String body = json(getTACSTestCharacterVO());
        mockMvc.perform(post("/users/" + id2 + "/characters/favorites?access_token=" + token.getAccessToken())
                .content(body)
                .contentType(contentType))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostFavoritesNotFoundToken() throws Exception {
        String body = json(getTACSTestCharacterVO());
        mockMvc.perform(post("/users/123456789012345678901234/characters/favorites?access_token=1234")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostFavoritesNotFreshToken() throws Exception {
        String body = json(getTACSTestCharacterVO());
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        token = setNotFreshExpirationDateToToken(token);
        mockMvc.perform(post("/users/123456789012345678901234/characters/favorites?access_token=" + token.getAccessToken())
                .content(body)
                .contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostFavoritesOk() throws Exception {
        String body = json(getTACSTestCharacterVO());
        mockMvc.perform(post("/users/123456789012345678901234/characters/favorites?access_token=" + createAndLogInTACSTestUser().getAccessToken())
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(1011334)))
                .andExpect(jsonPath("$.name", is("3-D Man")));
    }

    @Test
    public void testPostFavoritesOkAlreadyInDatabase() throws Exception {
        String body = json(getTACSTestCharacterVO());
        Token token = createAndLogInTACSTestUserWithFavorite(createTACSTestCharacter());
        mockMvc.perform(post("/users/123456789012345678901234/characters/favorites?access_token=" + token.getAccessToken())
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(1011334)))
                .andExpect(jsonPath("$.name", is("3-D Man")));
    }

    @Test
    public void testDeleteFavoritesTokenMustBeProvided() throws Exception {
        mockMvc.perform(delete("/users/123/characters/favorites/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFavoritesMismatchToken() throws Exception {
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        mockMvc.perform(delete("/users/" + id2 + "/characters/favorites/1?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFavoritesNotFoundToken() throws Exception {
        mockMvc.perform(delete("/users/123456789012345678901234/characters/favorites/1?access_token=1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFavoritesNotFreshToken() throws Exception {
        Token token = createAndLogInTACSTestUserWithNotFreshToken();
        mockMvc.perform(delete("/users/123456789012345678901234/characters/favorites/1?access_token=" + token.getAccessToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFavoritesOk() throws Exception {
        Character character1 = createTACSTestCharacter();
        Token token = createAndLogInTACSTestUserWithFavorite(character1);
        mockMvc.perform(delete("/users/123456789012345678901234/characters/favorites/1011334?access_token=" + token.getAccessToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFavoritesNotAFavorite() throws Exception {
        mockMvc.perform(delete("/users/123456789012345678901234/characters/favorites/123?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to remove character")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("character_not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFavoritesInvalidId() throws Exception {
        mockMvc.perform(delete("/users/123456789012345678901234/characters/favorites/abc?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to remove character")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_id")))
                .andExpect(jsonPath("$.cause", is(Collections.singletonList("character_id_must_be_a_natural_number"))));
    }

    @Test
    public void testPostNewTeamTokenMustBeProvided() throws Exception {
        String body = json(getTACSTestTeamWithMemberVO());
        mockMvc.perform(post("/users/123/teams")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostNewTeamMismatchToken() throws Exception {
        String body = json(getTACSTestTeamWithMemberVO());
        Token token = createAndLogInTACSTestUser();
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        mockMvc.perform(post("/users/" + id2 + "/teams?access_token=" + token.getAccessToken())
                .content(body)
                .contentType(contentType))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostNewTeamNotFoundToken() throws Exception {
        String body = json(getTACSTestTeamWithMemberVO());
        mockMvc.perform(post("/users/123456789012345678901234/teams?access_token=1234")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostNewTeamNotFreshToken() throws Exception {
        String body = json(getTACSTestTeamWithMemberVO());
        Token token = createAndLogInTACSTestUserWithNotFreshToken();
        mockMvc.perform(post("/users/" + getTACDId() + "/teams?access_token=" + token.getAccessToken())
                .content(body)
                .contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostNewTeamOk() throws Exception {
        String body = json(getTACSTestTeamWithMemberVO());
        mockMvc.perform(post("/users/" + getTACDId() + "/teams?access_token=" + createAndLogInTACSTestUser().getAccessToken()).content(body).contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.team_name", is("uno")))
                .andExpect(jsonPath("$.members", hasSize(1)));
    }

    @Test
    public void testGetTeamTokenMustBeProvided() throws Exception {
        mockMvc.perform(get("/users/123/teams/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamMismatchToken() throws Exception {
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        mockMvc.perform(get("/users/" + id2 + "/teams/1?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamNotFoundToken() throws Exception {
        mockMvc.perform(get("/users/" + getTACDId() + "/teams/1?access_token=1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamInvalidId() throws Exception {
        mockMvc.perform(get("/users/" + getTACDId() + "/teams/asd?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid id asd")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamInvalidIdNotFound() throws Exception {
        mockMvc.perform(get("/users/" + getTACDId() + "/teams/123456734512345678901234?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Team with id 123456734512345678901234 was not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamNotFreshToken() throws Exception {
        Token token = createAndLogInTACSTestUserWithNotFreshToken();
        mockMvc.perform(get("/users/" + getTACDId() + "/teams/1?access_token=" + token.getAccessToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamOk() throws Exception {
        Team team1 = createTACSTestTeamWithMember();
        Token token = createAndLogInTACSTestUserWithTeam(team1);
        mockMvc.perform(get("/users/" + getTACDId() + "/teams/" + team1.getTeamId() + "?access_token=" + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.team_name", is("uno")))
                .andExpect(jsonPath("$.members", hasSize(1)));
    }

    @Test
    public void testGetTeamDoesNotBelongToThatUser() throws Exception {
        Team team1 = createTACSTestTeamWithMember();
        Token token = createAndLogInTACSTestUserWithFavorite(team1.getMembers().get(0));
        mockMvc.perform(get("/users/" + getTACDId() + "/teams/" + team1.getTeamId() + "?access_token=" + token.getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Team with id " + team1.getTeamId() + " was not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamsTokenMustBeProvided() throws Exception {
        mockMvc.perform(get("/users/123/teams"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamsMismatchToken() throws Exception {
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        mockMvc.perform(get("/users/" + id2 + "/teams?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamsNotFoundToken() throws Exception {
        mockMvc.perform(get("/users/" + getTACDId() + "/teams?access_token=1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamsNotFreshToken() throws Exception {
        Token token = createAndLogInTACSTestUserWithNotFreshToken();
        mockMvc.perform(get("/users/" + getTACDId() + "/teams?access_token=" + token.getAccessToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetTeamsOk() throws Exception {
        Team team1 = createTACSTestTeamWithMember();
        Token token = createAndLogInTACSTestUserWithTeam(team1);
        mockMvc.perform(get("/users/" + getTACDId() + "/teams?access_token=" + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].team_name", is("uno")))
                .andExpect(jsonPath("$[0].members", hasSize(1)));
    }

    @Test
    public void testPostTeamTokenMustBeProvided() throws Exception {
        String body = json(getTACSTestCharacterVO());
        mockMvc.perform(post("/users/123/teams/1/characters")
                .content(body).contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostTeamMismatchToken() throws Exception {
        String body = json(getTACSTestCharacterVO());
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        mockMvc.perform(post("/users/" + id2 + "/teams/1/characters?access_token=" + createAndLogInTACSTestUser().getAccessToken())
                .content(body).contentType(contentType))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostTeamNotFoundToken() throws Exception {
        String body = json(getTACSTestCharacterVO());
        mockMvc.perform(post("/users/" + getTACDId() + "/teams/1/characters?access_token=1234").content(body).contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostTeamInvalidId() throws Exception {
        String body = json(getTACSTestCharacterVO());
        mockMvc.perform(post("/users/" + getTACDId() + "/teams/asd/characters?access_token=" + createAndLogInTACSTestUser().getAccessToken()).content(body).contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid id asd")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostTeamInvalidIdNotFound() throws Exception {
        String body = json(getTACSTestCharacterVO());
        mockMvc.perform(post("/users/" + getTACDId() + "/teams/123456734512345678901234/characters?access_token=" + createAndLogInTACSTestUser().getAccessToken()).content(body).contentType(contentType))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Team with id 123456734512345678901234 was not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostTeamNotFreshToken() throws Exception {
        String body = json(getTACSTestCharacterVO());
        Token token = createAndLogInTACSTestUserWithNotFreshToken();
        mockMvc.perform(post("/users/" + getTACDId() + "/teams/1/characters?access_token=" + token.getAccessToken()).content(body).contentType(contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testPostTeamOk() throws Exception {
        Character character = getTACSTestCharacterVO();
        character.setId(1011335);
        String body = json(character);
        Team team1 = createTACSTestTeamWithMember();
        Token token = createAndLogInTACSTestUserWithTeam(team1);
        mockMvc.perform(post("/users/" + getTACDId() + "/teams/" + team1.getTeamId() + "/characters?access_token=" + token.getAccessToken()).content(body).contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1011335)))
                .andExpect(jsonPath("$.name", is("3-D Man")));
    }

    @Test
    public void testPostTeamDoesNotBelongToThatUser() throws Exception {
        String body = json(getTACSTestCharacterVO());
        Team team1 = createTACSTestTeamWithMember();
        mockMvc.perform(post("/users/" + getTACDId() + "/teams/" + team1.getTeamId() + "/characters?access_token=" + createAndLogInTACSTestUser().getAccessToken()).content(body).contentType(contentType))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Team with id " + team1.getTeamId() + " was not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFromTeamTokenMustBeProvided() throws Exception {
        mockMvc.perform(delete("/users/123/teams/1/characters/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFromTeamMismatchToken() throws Exception {
        String id2 = "012345678901234567890000";
        User user2 = new User("TACS2", "testPass123;");
        User.validateUser(user2);
        ds.getDatastore().save(user2);
        mockMvc.perform(delete("/users/" + id2 + "/teams/1/characters/1?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFromTeamNotFoundToken() throws Exception {
        mockMvc.perform(delete("/users/" + getTACDId() + "/teams/1/characters/1?access_token=1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFromTeamInvalidId() throws Exception {
        mockMvc.perform(delete("/users/" + getTACDId() + "/teams/asd/characters/1?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid id asd")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFromTeamInvalidIdNotFound() throws Exception {
        mockMvc.perform(delete("/users/" + getTACDId() + "/teams/123456734512345678901234/characters/1?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Team with id 123456734512345678901234 was not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFromTeamNotFreshToken() throws Exception {
        Token token = createAndLogInTACSTestUserWithNotFreshToken();
        mockMvc.perform(delete("/users/" + getTACDId() + "/teams/1/characters/1?access_token=" + token.getAccessToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteFromTeamOk() throws Exception {
        Team team1 = createTACSTestTeamWithMember();
        Token token = createAndLogInTACSTestUserWithTeam(team1);
        mockMvc.perform(delete("/users/" + getTACDId() + "/teams/" + team1.getTeamId() + "/characters/1011334?access_token=" + token.getAccessToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFromTeamDoesNotBelongToThatUser() throws Exception {
        Team team1 = createTACSTestTeamWithMember();
        mockMvc.perform(delete("/users/" + getTACDId() + "/teams/" + team1.getTeamId() + "/characters/1?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Team with id " + team1.getTeamId() + " was not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testDeleteTeamOk() throws Exception {
        Team team1 = createTACSTestTeamWithMember();
        Token token = createAndLogInTACSTestUserWithTeam(team1);

        mockMvc.perform(get("/users/" + getTACDId() + "/teams/?access_token=" + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete("/users/" + getTACDId() + "/teams/"+ team1.getTeamId() +"/?access_token=" + token.getAccessToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/" + getTACDId() + "/teams/?access_token=" + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testBasicExceptionHandler() throws Exception {
        mockMvc.perform(delete("/users"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("Somethig went wrong!")))
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.error", is("internal_error")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testMalformedBodyExceptionHandler() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("id", "cualquierCosa");
        String body = json(request);
        mockMvc.perform(post("/users/" + getTACDId() + "/teams/1/characters?access_token=1234").content(body).contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid body, check the structure.")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetAllTeamsOk() throws Exception {
        Character character = getTACSTestCharacterVO();
        character.setId(1011335);
        String body = json(character);
        Team team1 = createTACSTestTeamWithMember();
        Token adminToken = createAndLogInTACSAdminTestUser();

        mockMvc.perform(post("/users/" + getTACDId() + "/teams/" + team1.getTeamId() + "/characters?access_token=" + adminToken.getAccessToken()).content(body).contentType(contentType));
        mockMvc.perform(get("/teams?access_token=" + adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].team_id", is(team1.getTeamId().toString())))
                .andExpect(jsonPath("$[0].team_name" , is("uno")))
                .andExpect(jsonPath("$[0].members").doesNotExist());
    }

    @Test
    public void testGetAllUsersOk() throws Exception {
        Token adminToken = createAndLogInTACSAdminTestUser();
        mockMvc.perform(get("/users?access_token=" + adminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("TACS")))
                .andExpect(jsonPath("$[0].admin", is(true)))
                .andExpect(jsonPath("$[0].user_id", is("123456789012345678901234")));
    }

    @Test
    public void testGetAllUsersInvalidCredentialsShouldFail() throws Exception {
        Token notAdminToken = createAndLogInTACSTestUser();
        mockMvc.perform(get("/users?access_token=" + notAdminToken.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    private List<Character> getCharactersPool() {
        List<Character> characters = new ArrayList<>();
        Character character1 = new Character();
        Thumbnail thumbnail1 = new Thumbnail();
        Character character2 = new Character();
        Thumbnail thumbnail2 = new Thumbnail();
        Character character3 = new Character();
        Thumbnail thumbnail3 = new Thumbnail();
        Character character4 = new Character();
        Thumbnail thumbnail4 = new Thumbnail();
        Character character5 = new Character();
        Thumbnail thumbnail5 = new Thumbnail();
        character1.setThumbnail(thumbnail1);
        character1.setId(1011334);
        character1.setName("3-D Man");
        thumbnail1.setPath("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784");
        thumbnail1.setExtension("JPG");
        character2.setThumbnail(thumbnail2);
        character2.setId(1011335);
        character2.setName("Iron Man");
        thumbnail2.setPath("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9785");
        thumbnail2.setExtension("JPG");
        character3.setThumbnail(thumbnail3);
        character3.setId(1011336);
        character3.setName("Batman");
        thumbnail3.setPath("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9786");
        thumbnail3.setExtension("JPG");
        character4.setThumbnail(thumbnail4);
        character4.setId(1011337);
        character4.setName("Captain America");
        thumbnail4.setPath("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9787");
        thumbnail4.setExtension("JPG");
        character5.setThumbnail(thumbnail5);
        character5.setId(1011338);
        character5.setName("Ant Man");
        thumbnail5.setPath("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9788");
        thumbnail5.setExtension("JPG");
        characters.add(character1);
        characters.add(character2);
        characters.add(character3);
        characters.add(character4);
        characters.add(character5);
        return characters;
    }

}
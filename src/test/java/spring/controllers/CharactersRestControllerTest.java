package spring.controllers;

import domain.Character;
import org.junit.Test;
import spring.BaseRestTester;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by niko118 on 4/11/16.
 */
public class CharactersRestControllerTest extends BaseRestTester {

    @Test
    public void testGetCharactersOk() throws Exception {
        mockMvc.perform(get("/characters" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken())
                .param("offset", "0")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.status", is("Ok")))
                .andExpect(jsonPath("$.data.offset", is(0)))
                .andExpect(jsonPath("$.data.total", is(1485)))
                .andExpect(jsonPath("$.data.limit", is(10)))
                .andExpect(jsonPath("$.data.results[0].id", is(1011334)))
                .andExpect(jsonPath("$.data.results[0].name", is("3-D Man")))
                .andExpect(jsonPath("$.data.results[1].id", is(1017100)))
                .andExpect(jsonPath("$.data.results[1].name", is("A-Bomb (HAS)")))
                .andExpect(jsonPath("$.data.results[2].id", is(1009144)))
                .andExpect(jsonPath("$.data.results[2].name", is("A.I.M.")))
                .andExpect(jsonPath("$.data.results[3].id", is(1010699)))
                .andExpect(jsonPath("$.data.results[3].name", is("Aaron Stack")))
                .andExpect(jsonPath("$.data.results[4].id", is(1009146)))
                .andExpect(jsonPath("$.data.results[4].name", is("Abomination (Emil Blonsky)")))
                .andExpect(jsonPath("$.data.results[5].id", is(1016823)))
                .andExpect(jsonPath("$.data.results[5].name", is("Abomination (Ultimate)")))
                .andExpect(jsonPath("$.data.results[6].id", is(1009148)))
                .andExpect(jsonPath("$.data.results[6].name", is("Absorbing Man")))
                .andExpect(jsonPath("$.data.results[7].id", is(1009149)))
                .andExpect(jsonPath("$.data.results[7].name", is("Abyss")))
                .andExpect(jsonPath("$.data.results[8].id", is(1010903)))
                .andExpect(jsonPath("$.data.results[8].name", is("Abyss (Age of Apocalypse)")))
                .andExpect(jsonPath("$.data.results[9].id", is(1011266)))
                .andExpect(jsonPath("$.data.results[9].name", is("Adam Destine")));
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testGetCharactersOkNoParams() throws Exception {
        mockMvc.perform(get("/characters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.status", is("Ok")))
                .andExpect(jsonPath("$.data.offset", is(0)))
                .andExpect(jsonPath("$.data.total", is(1485)))
                .andExpect(jsonPath("$.data.limit", is(100)));
    }

    @Test
    public void testGetCharactersFailThrowsException() throws Exception {
        mockMvc.perform(get("/characters")
                .param("name_starts_with", "throwException"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void testGetCharactersFailBadSort() throws Exception {
        mockMvc.perform(get("/characters")
                .param("sort", "cualquiera"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter sort, the valid ones are modified and name")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersFailBadCriteria() throws Exception {
        mockMvc.perform(get("/characters")
                .param("criteria", "cualquiera"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter criteria, the valid ones are desc and asc")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersFailBadLimitNotANumber() throws Exception {
        mockMvc.perform(get("/characters")
                .param("limit", "cualquiera"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number between 1 and 100")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersFailBadLimitLowerThan1() throws Exception {
        mockMvc.perform(get("/characters")
                .param("limit", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number between 1 and 100")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersFailBadLimitHigherThan100() throws Exception {
        mockMvc.perform(get("/characters")
                .param("limit", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number between 1 and 100")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersFailBadOffsetNotANumber() throws Exception {
        mockMvc.perform(get("/characters")
                .param("offset", "cualquiera"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter offset, must be a number greater than 0")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetCharactersFailBadOffsetLowerThan0() throws Exception {
        mockMvc.perform(get("/characters")
                .param("offset", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter offset, must be a number greater than 0")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetRankingCharactersInvalidToken() throws Exception {
        mockMvc.perform(get("/characters/ranking" + "?access_token=123")
                .param("limit", "askjdba"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("invalid_token")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetRankingCharactersTokenMustBeProvided() throws Exception {
        mockMvc.perform(get("/characters/ranking")
                .param("limit", "askjdba"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Access token must be provided")))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetRankingCharactersNotAdmin() throws Exception {
        mockMvc.perform(get("/characters/ranking?access_token=" + createAndLogInTACSTestUser().getAccessToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Forbidden")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.error", is("unauthorized")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testGetRankingCharactersInvalidLimitLowerThan1() throws Exception {
        mockMvc.perform(get("/characters/ranking" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken())
                .param("limit", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number greater than 0")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testGetRankingCharactersInvalidLimitGreaterThan100() throws Exception {
        mockMvc.perform(get("/characters/ranking" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken())
                .param("limit", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number greater than 0")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testGetRankingCharactersInvalidLimitNotANumber() throws Exception {
        mockMvc.perform(get("/characters/ranking" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken())
                .param("limit", "askjdba"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number greater than 0")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testGetRankingCharacters() throws Exception {
        createCharacters(10);
        mockMvc.perform(get("/characters/ranking" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken())
                .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[4].id", is(5)));
        deleteCharacters(10);
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testGetRankingCharactersTotalIsLessThanAsked() throws Exception {
        createCharacters(5);
        mockMvc.perform(get("/characters/ranking" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken())
                .param("limit", "4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[3].id", is(4)));
        deleteCharacters(5);
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testGetRankingCharactersNoLimitParameter() throws Exception {
        createCharacters(4);
        mockMvc.perform(get("/characters/ranking" + "?access_token=" + createAndLogInTACSAdminTestUser().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[3].id", is(4)));
        deleteCharacters(4);
        deleteTACSTestUserWithToken();
    }

    private void createCharacters(int qty) {
        if (qty > 10) {
            throw new IllegalArgumentException("The quantity must be up to 10.");
        }
        Character character;
        for (int i = 1; i <= qty; i++) {
            character = new Character(i, "name" + i, "description" + i);
            character.setElectedTimes(qty - i);
            ds.getDatastore().save(character);
        }
    }

    private void deleteCharacters(int qty) {
        if (qty > 10) {
            throw new IllegalArgumentException("The quantity must be up to 10.");
        }
        for (int i = 1; i <= qty; i++) {
            ds.getDatastore().delete(ds.getDatastore().find(Character.class, "id", i));
        }
    }

}
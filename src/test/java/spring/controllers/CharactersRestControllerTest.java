package spring.controllers;

import domain.Character;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import services.DSMongoInterface;
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

    @Autowired
    private DSMongoInterface ds;

    @Test
    public void testGetCharactersOk() throws Exception {
        mockMvc.perform(get("/characters")
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
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Somethig went wrong!")))
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.error", is("internal_error")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
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
    public void testGetCharactersFailBadLimit1() throws Exception {
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
    public void testGetCharactersFailBadLimit2() throws Exception {
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
    public void testGetCharactersFailBadLimit3() throws Exception {
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
    public void testGetCharactersFailBadOffset1() throws Exception {
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
    public void testGetCharactersFailBadOffset3() throws Exception {
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
    public void testGetRankingCharactersInvalidLimit0() throws Exception {
        mockMvc.perform(get("/characters/ranking")
                .param("limit", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number greater than 0")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetRankingCharactersInvalidLimit101() throws Exception {
        mockMvc.perform(get("/characters/ranking")
                .param("limit", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number greater than 0")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetRankingCharactersInvalidLimit2() throws Exception {
        mockMvc.perform(get("/characters/ranking")
                .param("limit", "askjdba"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Invalid value for parameter limit, must be a number greater than 0")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("bad_request")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetRankingCharacters() throws Exception {
        Character character;
        character = new Character(1, "name1", "description1");
        character.setElectedTimes(10);
        ds.getDatastore().save(character);
        character = new Character(2, "name2", "description2");
        character.setElectedTimes(9);
        ds.getDatastore().save(character);
        character = new Character(3, "name3", "description3");
        character.setElectedTimes(8);
        ds.getDatastore().save(character);
        character = new Character(4, "name4", "description4");
        character.setElectedTimes(7);
        ds.getDatastore().save(character);
        character = new Character(5, "name5", "description5");
        character.setElectedTimes(6);
        ds.getDatastore().save(character);
        character = new Character(6, "name6", "description6");
        character.setElectedTimes(5);
        ds.getDatastore().save(character);
        character = new Character(7, "name7", "description7");
        character.setElectedTimes(4);
        ds.getDatastore().save(character);
        character = new Character(8, "name8", "description8");
        character.setElectedTimes(3);
        ds.getDatastore().save(character);
        character = new Character(9, "name9", "description9");
        character.setElectedTimes(2);
        ds.getDatastore().save(character);
        character = new Character(10, "name10", "description10");
        character.setElectedTimes(1);
        ds.getDatastore().save(character);
        mockMvc.perform(get("/characters/ranking")
                .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[4].id", is(5)));
        for (int i = 0; i < 10; i++) {
            ds.getDatastore().delete(ds.getDatastore().find(Character.class, "id", i));
        }
    }

    @Test
    public void testGetRankingCharactersTotalIsLessThanAsked() throws Exception {
        Character character;
        character = new Character(1, "name1", "description1");
        character.setElectedTimes(10);
        ds.getDatastore().save(character);
        character = new Character(2, "name2", "description2");
        character.setElectedTimes(9);
        ds.getDatastore().save(character);
        character = new Character(3, "name3", "description3");
        character.setElectedTimes(8);
        ds.getDatastore().save(character);
        character = new Character(4, "name4", "description4");
        character.setElectedTimes(7);
        ds.getDatastore().save(character);
        mockMvc.perform(get("/characters/ranking")
                .param("limit", "4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[3].id", is(4)));
        for (int i = 0; i < 5; i++) {
            ds.getDatastore().delete(ds.getDatastore().find(Character.class, "id", i));
        }
    }

    @Test
    public void testGetRankingCharactersNoLimitParameter() throws Exception {
        Character character;
        character = new Character(1, "name1", "description1");
        character.setElectedTimes(10);
        ds.getDatastore().save(character);
        character = new Character(2, "name2", "description2");
        character.setElectedTimes(9);
        ds.getDatastore().save(character);
        character = new Character(3, "name3", "description3");
        character.setElectedTimes(8);
        ds.getDatastore().save(character);
        character = new Character(4, "name4", "description4");
        character.setElectedTimes(7);
        ds.getDatastore().save(character);
        mockMvc.perform(get("/characters/ranking"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[3].id", is(4)));
        for (int i = 0; i < 5; i++) {
            ds.getDatastore().delete(ds.getDatastore().find(Character.class, "id", i));
        }
    }

}
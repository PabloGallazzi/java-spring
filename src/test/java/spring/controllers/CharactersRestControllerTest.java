package spring.controllers;

import org.junit.Test;
import spring.BaseRestTester;

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
    public void testGetCharacters() throws Exception {
        mockMvc.perform(get("/characters")
                .param("sort","created")
                .param("criteria","created")
                .param("offset","0")
                .param("limit","50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.paging.total", is(123)))
                .andExpect(jsonPath("$.paging.limit", is(50)))
                .andExpect(jsonPath("$.paging.offset", is(0)));
        //Have to test the characters return too.
    }

    @Test
    public void testGetRanking() throws Exception {
        mockMvc.perform(get("/characters/ranking"))
                .andExpect(status().isOk());
        //No sense to test it now
    }
}
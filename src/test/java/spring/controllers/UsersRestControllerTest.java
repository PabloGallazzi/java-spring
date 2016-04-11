package spring.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spring.BaseRestTester;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by niko118 on 4/11/16.
 */
public class UsersRestControllerTest extends BaseRestTester {


    @Test
    public void testCompareTeams() throws Exception {

    }

    @Test
    public void testCreateUser() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name","userTest");
        request.put("user_password",12345678);
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is("userTest")))
                .andExpect(jsonPath("$.user_id", is(1234)));
    }

    @Test
    public void testGetUserInfo() throws Exception {

    }

    @Test
    public void testGetFavorites() throws Exception {

    }

    @Test
    public void testAddFavorite() throws Exception {

    }

    @Test
    public void testRemoveFavorite() throws Exception {

    }

    @Test
    public void testCreateTeam() throws Exception {

    }

    @Test
    public void testGetTeam() throws Exception {

    }

    @Test
    public void testAddToTeam() throws Exception {

    }

    @Test
    public void testRemoveFromTeam() throws Exception {

    }
}
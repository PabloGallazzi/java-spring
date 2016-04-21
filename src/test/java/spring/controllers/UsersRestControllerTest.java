package spring.controllers;

import domain.User;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import services.DSMongoInterface;
import spring.BaseRestTester;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
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

    @Autowired
    private DSMongoInterface ds;

    /*@After
    public void tearDown() {
        ds.getDatastore().getDB().getCollection("users").getIndexInfo()
        ds.getDatastore().getDB().getCollection("users").remove();
        ds.getDatastore().getDB().createCollection("users");
        //ds.stopDatastore();
    }*/

    @Test
    public void testCreateUserSuccess() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "userTestSuccess");
        request.put("user_password", "12345678;");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is("userTestSuccess")));
        ds.getDatastore().delete(ds.getDatastore().find(User.class, "userName", "userTestSuccess"));
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
        request.put("user_name", "userTest");
        request.put("user_password", "12345678;");
        String body = json(request);
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is("userTest")));
        mockMvc.perform(post("/users")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to create user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation error")))
                .andExpect(jsonPath("$.cause", is(Collections.singletonList("user_name_already_used"))));
        ds.getDatastore().delete(ds.getDatastore().find(User.class, "userName", "userTest"));
    }

    @Test
    public void testUnableToGetIdLessThan24CharactersReturnsNotFound() throws Exception {
        mockMvc.perform(get("/users/123"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to find resource")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testUnableToGetRetrievesNotFound() throws Exception {
        mockMvc.perform(get("/users/123456789012345678901234"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to find resource")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("not_found")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testGetSuccessFull() throws Exception {
        String id = "123456789012345678901234";
        User user = new User("TACS", "test");
        ObjectId objectId = new ObjectId(id);
        user.setUserId(objectId);
        ds.getDatastore().save(user);
        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is("TACS")));
        ds.getDatastore().delete(ds.getDatastore().find(User.class, "userName", "TACS"));
    }

}
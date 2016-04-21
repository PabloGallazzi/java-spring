package spring.controllers;

import org.junit.Test;
import spring.BaseRestTester;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by niko118 on 4/11/16.
 */
public class UsersRestControllerTest extends BaseRestTester {

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
    }

}
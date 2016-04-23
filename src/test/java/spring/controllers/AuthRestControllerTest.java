package spring.controllers;

import domain.Token;
import domain.User;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import services.DSMongoInterface;
import spring.BaseRestTester;

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
public class AuthRestControllerTest extends BaseRestTester {

    @Autowired
    private DSMongoInterface ds;

    @Test
    public void testLoginSuccessFull() throws Exception {
        String id = "123456789012345678901234";
        User user = new User("TACS", "testPass123;");
        User.validateUser(user);
        ObjectId objectId = new ObjectId(id);
        user.setUserId(objectId);
        ds.getDatastore().save(user);
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "testPass123;");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_id", is("123456789012345678901234")));
        Token token = ds.getDatastore().find(Token.class, "userId", objectId).get();
        ds.getDatastore().delete(token);
        ds.getDatastore().delete(ds.getDatastore().find(User.class, "userName", "TACS"));
    }

    @Test
    public void testLoginSuccessFullResolvesTheSameTokenTwice() throws Exception {
        String id = "123456789012345678901234";
        User user = new User("TACS", "testPass123;");
        User.validateUser(user);
        ObjectId objectId = new ObjectId(id);
        user.setUserId(objectId);
        ds.getDatastore().save(user);
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "testPass123;");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_id", is("123456789012345678901234")));
        Token token = ds.getDatastore().find(Token.class, "userId", objectId).get();
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(token.getAccessToken())))
                .andExpect(jsonPath("$.user_id", is("123456789012345678901234")));
        ds.getDatastore().delete(token);
        ds.getDatastore().delete(ds.getDatastore().find(User.class, "userName", "TACS"));
    }

    @Test
    public void testLoginNoPasswordShouldThrowBadRequest() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to authenticate user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("invalid_credentials")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testLoginNoUserShouldThrowBadRequest() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_password", "testPass123;");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to authenticate user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("invalid_credentials")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testLoginEmptyPasswordShouldThrowBadRequest() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to authenticate user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("invalid_credentials")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testLoginEmptyUserShouldThrowBadRequest() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "");
        request.put("user_password", "testPass123;");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to authenticate user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("invalid_credentials")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testUserNotFoundShouldThrowBadRequest() throws Exception {
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "testPass123;");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to authenticate user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("invalid_credentials")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
    }

    @Test
    public void testWrongPasswordShouldThrowBadRequest() throws Exception {
        String id = "123456789012345678901234";
        User user = new User("TACS", "testPass123;");
        User.validateUser(user);
        ObjectId objectId = new ObjectId(id);
        user.setUserId(objectId);
        ds.getDatastore().save(user);
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "wrongTestPass;");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message", is("Unable to authenticate user")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("invalid_credentials")))
                .andExpect(jsonPath("$.cause", is(Collections.emptyList())));
        ds.getDatastore().delete(ds.getDatastore().find(User.class, "userName", "TACS"));
    }

}
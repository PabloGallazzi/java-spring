package spring.controllers;

import domain.Token;
import domain.User;
import org.junit.Test;
import spring.BaseRestTester;
import spring.utils.ScopesHelper;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by niko118 on 4/11/16.
 */
public class AuthRestControllerTest extends BaseRestTester {

    @Test
    public void testLoginSuccessFullNotAdmin() throws Exception {
        createTACSTestUser();
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "testPass123;");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_id", is("123456789012345678901234")))
                .andExpect(jsonPath("$.scopes", is(Arrays.asList(ScopesHelper.READ, ScopesHelper.WRITE))));
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testLoginSuccessFullAdmin() throws Exception {
        createTACSAdminTestUser();
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("user_name", "TACS");
        request.put("user_password", "testPass123;");
        String body = json(request);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_id", is("123456789012345678901234")))
                .andExpect(jsonPath("$.scopes", is(Arrays.asList(ScopesHelper.READ, ScopesHelper.WRITE, ScopesHelper.ADMIN))));
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testLoginSuccessFullExpiredToken() throws Exception {
        User user = createTACSTestUser();
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
        Token token = ds.getDatastore().find(Token.class, "userId", user.getUserId()).get();
        token.setExpirationDate(new Date(new Date().getTime() - 1));
        ds.getDatastore().save(token);
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", not(token.getAccessToken())))
                .andExpect(jsonPath("$.user_id", is("123456789012345678901234")));
        deleteTACSTestUserWithToken();
    }

    @Test
    public void testLoginSuccessFullResolvesTheSameTokenTwice() throws Exception {
        createTACSTestUser();
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
        Token token = getTACSTestUserToken();
        mockMvc.perform(post("/users/authenticate")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(token.getAccessToken())))
                .andExpect(jsonPath("$.user_id", is("123456789012345678901234")));
        deleteTACSTestUserWithToken();
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
        createTACSTestUser();
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
        deleteTACSTestUser();
    }

}
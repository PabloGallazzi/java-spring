package spring.controllers;

import domain.User;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import services.DSMongoInterface;
import spring.BaseRestTester;

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
    public void testLogin() throws Exception {
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

    }
}
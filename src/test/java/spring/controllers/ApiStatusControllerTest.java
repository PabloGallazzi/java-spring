package spring.controllers;

import org.junit.Test;
import spring.BaseRestTester;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pgallazzi on 26/4/16.
 */
public class ApiStatusControllerTest extends BaseRestTester {

    @Test
    public void testGetCharactersIntersectionBadIds() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

}

package spring.controllers.poc;

import org.junit.Test;
import spring.BaseTester;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;

/**
 * Created by pgallazzi on 3/4/16.
 */
public class PocWebControllerTest extends BaseTester {

    //This is a POC test that shows how to test simple model passed from the controller
    @Test
    public void testGreetingWeb() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(view().name("greeting"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attribute("title", is("POC WEB")))
                .andExpect(model().attributeExists("instance"))
                .andExpect(model().attribute("instance",
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("name", is("Pablo")),
                                hasProperty("age", is(23)),
                                hasProperty("hobbies", is(Arrays.asList("dormir", "comer", "nada"))),
                                hasProperty("surname", is("Gallazzi"))
                        )
                ));
    }

    //This is a POC test to check that the custom web error handler is working
    @Test
    public void testGreetingErrorWeb() throws Exception {
        mockMvc.perform(get("/greetingError"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("url"))
                .andExpect(model().attributeExists("timestamp"))
                .andExpect(model().attributeExists("status"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("url", is("http://localhost/greetingError")))
                .andExpect(model().attribute("status", is(404)))
                .andExpect(model().attribute("error", is("Testing web error")));
    }
}

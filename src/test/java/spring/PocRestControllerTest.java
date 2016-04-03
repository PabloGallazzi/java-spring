package spring;

import domain.PocVo;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


/**
 * Created by pgallazzi on 2/4/16.
 */
public class PocRestControllerTest extends BaseRestTester {

    //POC test to assert only one item
    @Test
    public void readSinglePocVo() throws Exception {
        mockMvc.perform(get("/poc/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Pablo")))
                .andExpect(jsonPath("$.surname", is("Gallazzi")))
                .andExpect(jsonPath("$.age", is(23)))
                .andExpect(jsonPath("$.hobbies", is(Arrays.asList("dormir", "comer", "nada"))));
    }

    //POC test to assert an array of items
    @Test
    public void readAllPocVo() throws Exception {
        mockMvc.perform(get("/poc/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Pablo")))
                .andExpect(jsonPath("$[0].surname", is("Gallazzi")))
                .andExpect(jsonPath("$[0].age", is(23)))
                .andExpect(jsonPath("$[0].hobbies", is(Arrays.asList("dormir", "comer", "nada"))))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Nico")))
                .andExpect(jsonPath("$[1].surname", is("Garcia")))
                .andExpect(jsonPath("$[1].age", is(23)))
                .andExpect(jsonPath("$[1].hobbies", is(Arrays.asList("dormir", "comer", "nada"))));
    }

    //This POC test shows how to test a post request with a body, note that the id shouldn't be passed in the body...
    @Test
    public void postNewData() throws Exception {
        List<String> pocHobbies = Arrays.asList("dormir", "comer", "nada");
        PocVo pocInstance = new PocVo(1, "Pablo", "Gallazzi", 23, pocHobbies);
        String body = json(pocInstance);
        mockMvc.perform(post("/poc")
                .content(body)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Pablo")))
                .andExpect(jsonPath("$.surname", is("Gallazzi")))
                .andExpect(jsonPath("$.age", is(23)))
                .andExpect(jsonPath("$.hobbies", is(Arrays.asList("dormir", "comer", "nada"))));
    }

}

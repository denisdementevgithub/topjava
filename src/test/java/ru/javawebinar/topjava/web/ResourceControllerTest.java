package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    void getResources() throws Exception {
        perform(get("/resources/resources/css/style.css"))
                .andExpect(header().string("Content-Type", containsString("text/css")))
                .andExpect(status().isOk());
    }
}

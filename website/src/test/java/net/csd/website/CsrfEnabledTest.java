package net.csd.website;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import net.csd.website.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


@ContextConfiguration(classes = { SecurityConfig.class })
public class CsrfEnabledTest {

    @Autowired
    MockMvc mvc;
    
    @Test
    public void givenNoCsrf_whenAddFoo_thenForbidden() throws Exception {
        // @formatter:off
        mvc
        .perform(post("/auth/foos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(createFoo())
        .with(testUser()))
        .andExpect(status().isForbidden());
        // @formatter:on
    }

    @Test
    public void givenCsrf_whenAddFoo_thenCreated() throws Exception {
        // @formatter:off
        mvc
        .perform(post("/auth/foos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(createFoo())
        .with(testUser())
        .with(csrf()))
        .andExpect(status().isCreated());
        // @formatter:on
    }
}

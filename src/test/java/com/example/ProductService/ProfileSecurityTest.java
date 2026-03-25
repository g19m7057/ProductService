package com.example.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.annotation.DirtiesContext(classMode = org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProfileSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerShouldBePublic() throws Exception {
        String email = "test-" + System.currentTimeMillis() + "@example.com";
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"" + email + "\", \"password\": \"password\", \"name\": \"Test User\", \"dob\": \"02/02/1991\", \"isSouthAfrican\": true, \"contactNumber\": \"123\", \"identificationNumber\": \"456\", \"address\": \"Addr\", \"customerType\": \"01\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getProfilesShouldBePublic() throws Exception {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsShouldBeProtected() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());
    }
}

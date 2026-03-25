package com.example.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CorrelationIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateCorrelationIdWhenMissing() throws Exception {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Correlation-ID"));
    }

    @Test
    void shouldReturnProvidedCorrelationId() throws Exception {
        String testCorrelationId = "test-id-123";
        mockMvc.perform(get("/auth")
                .header("X-Correlation-ID", testCorrelationId))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Correlation-ID", testCorrelationId));
    }

    @Test
    void shouldIncludeCorrelationIdInExceptionResponse() throws Exception {
        String testCorrelationId = "error-id-456";
        mockMvc.perform(post("/auth/login") 
                .header("X-Correlation-ID", testCorrelationId)
                .contentType("application/json")
                .content("{\"email\": \"nonexistent@example.com\", \"password\": \"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.correlationId").value(testCorrelationId));
    }
}

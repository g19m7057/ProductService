package com.example.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@SpringBootTest
@AutoConfigureWebTestClient
@org.springframework.test.annotation.DirtiesContext(classMode = org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProfileSecurityTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void registerShouldBePublic() throws Exception {
        String email = "test-" + System.currentTimeMillis() + "@example.com";
        String body = "{\"email\": \"" + email + "\", \"password\": \"password\", \"name\": \"Test User\", \"dob\": \"1991-02-02\", \"countryCode\": \"SA\", \"contactNumber\": \"123\", \"identificationNumber\": \"456\", \"address\": \"Addr\", \"customerType\": 1, \"maritalStatus\": \"SINGLE\"}";
        webTestClient.post().uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(body))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void getProfilesShouldBePublic() throws Exception {
        webTestClient.get().uri("/auth")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getProductsShouldBePublicButFiltered() throws Exception {
        webTestClient.get().uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Map.class)
                .value(products -> {
                    // Public products (no requirements) should be visible
                    assertTrue(products.stream().anyMatch(p -> String.valueOf(p.get("name")).equalsIgnoreCase("Standard Savings Account")));
                    // Products with requirements should NOT be visible to anonymous
                    assertTrue(products.stream().noneMatch(p -> String.valueOf(p.get("name")).equalsIgnoreCase("Gold Credit Card")));
                    assertTrue(products.stream().noneMatch(p -> String.valueOf(p.get("name")).equalsIgnoreCase("Business Credit Card")));
                });
    }

    @Test
    void getProductsShouldFilterByAge() throws Exception {
        // Register an underage user (e.g., 5 years old)
        String email = "underage-" + System.currentTimeMillis() + "@example.com";
        String body = "{\"email\": \"" + email + "\", \"password\": \"password\", \"name\": \"Young User\", \"dob\": \"2021-02-02\", \"countryCode\": \"ZA\", \"contactNumber\": \"123\", \"identificationNumber\": \"456\", \"address\": \"Addr\", \"customerType\": 1, \"maritalStatus\": \"SINGLE\"}";
        
        EntityExchangeResult<Map> result = webTestClient.post().uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(body))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Map.class)
                .returnResult();

        String token = (String) result.getResponseBody().get("token");

        webTestClient.get().uri("/products")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Map.class)
                .value(products -> {
                    // Underage user should NOT see Gold Credit Card (min_age 18)
                    assertTrue(products.stream().noneMatch(p -> String.valueOf(p.get("name")).equalsIgnoreCase("Gold Credit Card")));
                    // Should see Standard Savings Account
                    assertTrue(products.stream().anyMatch(p -> String.valueOf(p.get("name")).equalsIgnoreCase("Standard Savings Account")));
                });
    }

    @Test
    void getProductsShouldFilterByCustomerType() throws Exception {
        // Register an Individual user (type 1)
        String email = "individual-" + System.currentTimeMillis() + "@example.com";
        String body = "{\"email\": \"" + email + "\", \"password\": \"password\", \"name\": \"Ind User\", \"dob\": \"1990-02-02\", \"countryCode\": \"ZA\", \"contactNumber\": \"123\", \"identificationNumber\": \"456\", \"address\": \"Addr\", \"customerType\": 1, \"maritalStatus\": \"SINGLE\"}";
        
        EntityExchangeResult<Map> result = webTestClient.post().uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(body))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Map.class)
                .returnResult();

        String token = (String) result.getResponseBody().get("token");

        webTestClient.get().uri("/products")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Map.class)
                .value(products -> {
                    // Individual should NOT see Business Credit Card (required type 2)
                    assertTrue(products.stream().noneMatch(p -> String.valueOf(p.get("name")).equalsIgnoreCase("Business Credit Card")));
                    // Should see Gold Credit Card (if 18+)
                    assertTrue(products.stream().anyMatch(p -> String.valueOf(p.get("name")).equalsIgnoreCase("Gold Credit Card")));
                });
    }
}

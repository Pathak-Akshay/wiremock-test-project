package org.example.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.example.service.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

public class WiremockTest {

    private static final Logger logger = LoggerFactory.getLogger(WiremockTest.class);
    private WireMockServer wireMockServer;
    private ApiClient apiClient;
    private static final int PORT = 8089;

    @BeforeClass
    public void setup(){

        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(PORT)
                .withRootDirectory("src/test/resources")
                .globalTemplating(true));
        wireMockServer.start();

        WireMock.configureFor("localhost",PORT);

        apiClient = new ApiClient("http://localhost:" + PORT);

        logger.info("Started Wiremock server on port {}", PORT);
    }

    @AfterClass
    public void teardown() throws IOException{

        // Stop WireMock server and close API client
        if(apiClient != null){
            apiClient.close();
        }

        if (wireMockServer != null && wireMockServer.isRunning()){
            wireMockServer.stop();
        }
    }

    @Test
    public void testGetUserById() throws IOException{

        // The stub for this is defined in src/test/resources/mappings/user-get.json
        // Test the API client with the mock

        Map<String, Object> user = apiClient.getUserById(1);

        assertNotNull(user);
        assertEquals(user.get("id"), 1);
        assertEquals(user.get("name"), "Jane Smith");
        assertEquals(user.get("email"), "jane.smith@example.com");
        assertEquals(user.get("age"), 28);
        assertEquals(user.get("department"), "Engineering");

        // Verify the request was made
        verify(getRequestedFor(urlPathEqualTo("/api/users/1")));
    }

    @Test
    public void testCreateUser() throws IOException{

        // The stub for this is defined in src/test/resources/mappings/user-post.json
        // Create test user data
        Map<String, Object> userData = ApiClient.createTestUser();

        // Test the API client with the mock
        Map<String, Object> createdUser = apiClient.createUser(userData);

        // Assertions
        assertNotNull(createdUser);
        assertEquals(createdUser.get("id"), 123);
        assertEquals(userData.get("name"), createdUser.get("name"));
        assertEquals(userData.get("email"), createdUser.get("email"));
        assertNotNull(createdUser.get("createdAt"));

        // Verify the request was made with the correct body
        verify(postRequestedFor(urlEqualTo("/api/users"))
                .withHeader("Content-Type", containing("application/json")));
    }

    @Test
    public void testGetUserWithProgrammaticStub() throws IOException {
        // Create a stub programmatically instead of using JSON files
        int userId = 42;

        stubFor(get(urlPathEqualTo("/api/users/" + userId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"id\": " + userId + ",\n" +
                                "  \"name\": \"Programmatic User\",\n" +
                                "  \"email\": \"programmatic@example.com\",\n" +
                                "  \"age\": 35,\n" +
                                "  \"role\": \"Developer\"\n" +
                                "}")));

        // Test the API client with the programmatic mock
        Map<String, Object> user = apiClient.getUserById(userId);

        // Assertions
        assertNotNull(user);
        assertEquals(user.get("id"), userId);
        assertEquals( user.get("name"), "Programmatic User");
        assertEquals( user.get("email"), "programmatic@example.com");
        assertEquals( user.get("age"), 35);
        assertEquals( user.get("role"), "Developer");

        // Verify the request was made
        verify(getRequestedFor(urlPathEqualTo("/api/users/" + userId)));
    }
}

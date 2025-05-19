package org.example.test;

import org.example.service.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class DockerizedWiremockTest {

    private static final Logger logger = LoggerFactory.getLogger(DockerizedWiremockTest.class);
    private ApiClient apiClient;

    // Port where the Wiremock container is running
    // In docker-compose.yml, we map container port 8080 to host port 8080
    private static final int WIREMOCK_PORT = 9090;
    private static final String WIREMOCK_HOST = "localhost";

    @BeforeClass
    public void setup() {
        // Initialize API client pointing to the Docker container
        apiClient = new ApiClient("http://" + WIREMOCK_HOST + ":" + WIREMOCK_PORT);
        logger.info("Configured API client to use Wiremock container at {}:{}", WIREMOCK_HOST, WIREMOCK_PORT);
    }

    @AfterClass
    public void teardown() throws IOException {
        if (apiClient != null) {
            apiClient.close();
        }
    }

    @Test
    public void testGetUserFromContainer() throws IOException {
        // Test the API client with the containerized Wiremock
        Map<String, Object> user = apiClient.getUserById(1);

        // Assertions
        assertNotNull(user);
        assertEquals( user.get("id"), 1);
        assertEquals( user.get("name"), "Jane Smith");
        assertEquals( user.get("email"), "jane.smith@example.com");
    }

    @Test
    public void testCreateUserWithContainer() throws IOException {
        // Create test user data
        Map<String, Object> userData = ApiClient.createTestUser();

        // Test the API client with the containerized Wiremock
        Map<String, Object> createdUser = apiClient.createUser(userData);

        // Assertions
        assertNotNull(createdUser);
        assertEquals( createdUser.get("id"), 123);
        assertEquals(userData.get("name"), createdUser.get("name"));
        assertEquals(userData.get("email"), createdUser.get("email"));
        assertNotNull(createdUser.get("createdAt"));
    }
}

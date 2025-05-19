package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple API client that will be tested with mocks
 */
public class ApiClient {
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final String baseUrl;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Gets user data by user ID
     */
    public Map<String, Object> getUserById(int userId) throws IOException {
        String url = baseUrl + "/api/users/" + userId;
        HttpGet request = new HttpGet(url);

        logger.info("Sending GET request to: {}", url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String responseBody = EntityUtils.toString(entity);
                logger.info("Response: {}", responseBody);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    return objectMapper.readValue(responseBody, Map.class);
                } else {
                    throw new IOException("Unexpected response status: " + statusCode);
                }
            } else {
                throw new IOException("Empty response");
            }
        }
    }

    /**
     * Creates a new user
     */
    public Map<String, Object> createUser(Map<String, Object> userData) throws IOException {
        String url = baseUrl + "/api/users";
        HttpPost request = new HttpPost(url);

        try {
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(userData));
            request.setEntity(entity);
            request.setHeader("Content-Type", "application/json");

            logger.info("Sending POST request to: {}", url);
            logger.info("Request body: {}", objectMapper.writeValueAsString(userData));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity responseEntity = response.getEntity();

                if (responseEntity != null) {
                    String responseBody = EntityUtils.toString(responseEntity);
                    logger.info("Response: {}", responseBody);

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 201) {
                        return objectMapper.readValue(responseBody, Map.class);
                    } else {
                        throw new IOException("Unexpected response status: " + statusCode);
                    }
                } else {
                    throw new IOException("Empty response");
                }
            }
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new IOException("Failed to create request", e);
        }
    }

    /**
     * Creates a user for testing purposes
     */
    public static Map<String, Object> createTestUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "John Doe");
        user.put("email", "john.doe@example.com");
        user.put("age", 30);
        return user;
    }

    /**
     * Close the HTTP client
     */
    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }
}
# Wiremock Test Environment
A comprehensive test framework that demonstrates how to use Wiremock for service simulation with Java, TestNG, and Docker.

## Overview
This repository provides a complete solution for setting up reliable and efficient tests for your microservices using Wiremock. It demonstrates best practices for service virtualization, allowing you to:

- Simulate API responses for consistent testing
- Run tests in isolation without dependencies on external services
- Containerize your test environment for consistency across platforms
- Implement robust test patterns for microservice architectures

## Features

- **Wiremock Integration**: Mock HTTP services with precise control over requests and responses
- **TestNG Framework**: Structured test organization with powerful assertion capabilities
- **Docker Support**: Containerized test execution for consistent environments
- **Example Test Cases**: Demonstrations of both local and dockerized Wiremock tests

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for containerized tests)

## Getting Started
**1. Clone the Repository**
- git clone https://github.com/yourusername/wiremock-test-project.git
- cd wiremock-test-project
  
**2. Run Local Tests**
- mvn clean test
- This will execute the TestNG test suite using a local Wiremock instance.
  
**3. Run Containerized Tests**
- docker-compose up
- This command builds and runs the tests in a Docker container with a separate Wiremock container.

## Key Components
### ApiClient
A simple HTTP client that interacts with APIs (real or mocked). This is the component we test against Wiremock.

### Wiremock Mappings
JSON files defining request matching and response generation:

- user-get.json: Simulates GET requests for user data
- user-post.json: Simulates POST requests for creating users

### Test Classes

- WiremockTest.java: Demonstrates testing against a local Wiremock instance
- DockerizedWiremockTest.java: Shows how to test against a Wiremock instance running in Docker

## Configuration
### TestNG Configuration
The testng.xml file organizes the test suites and controls test execution flow.

### Docker Configuration

- Dockerfile: Defines the container for running the tests
- docker-compose.yml: Sets up the multi-container environment with separate services for tests and Wiremock

## Advanced Usage
### Custom Wiremock Behaviors
Extend the example by implementing:

- Response templating
- Request verification
- Stateful behavior
- Response delays

## CI/CD Integration
This project is designed to be easily integrated into CI/CD pipelines. The Docker-based approach ensures consistent test execution across environments.

## Troubleshooting
### Common Issues

1. Connection refused to Wiremock

- Ensure Wiremock is running on the expected port
- Check network settings in Docker Compose if using containerized tests

2. Tests failing inconsistently

- Verify Wiremock mappings are correctly defined
- Ensure proper test isolation between test methods

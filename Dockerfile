FROM maven:3.8-openjdk-11

WORKDIR /app

# Copy the Maven POM file
COPY pom.xml .

# Download all required dependencies
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Run the tests
CMD ["mvn", "test"]

# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.8-jdk-11 as builder

# Set the maximum heap size to 75% of the container's memory
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0"
#ENV JAVA_TOOL_OPTIONS "-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk11:alpine-jre

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/salescommissionrebuildbackend-*.jar /salescommissionrebuildbackend-0.0.1-SNAPSHOT.jar

# Run the web service on container startup.
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/salescommissionrebuildbackend-0.0.1-SNAPSHOT.jar"]
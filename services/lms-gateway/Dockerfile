# Build stage
FROM maven:3.8.7-openjdk-18 AS build

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -Dskiptests

# Runtime stage
FROM amazoncorretto:17

# Arguments
ARG PROFILE=dev
ARG APP_VERSION=1.0.5

WORKDIR /app
COPY --from=build /build/target/lms-gateway-*.jar /app/

EXPOSE 8055
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}


CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} lms-gateway-${JAR_VERSION}.jar
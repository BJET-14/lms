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
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/lms-common-service-*.jar /app/

EXPOSE 8053
ENV DB_URL=jdbc:postgresql://lms-postgres:5432/lms
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}


CMD java -jar -Dspring.profile.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} lms-common-service-${JAR_VERSION}.jar
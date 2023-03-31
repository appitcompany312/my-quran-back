FROM maven:3.8.1-openjdk-17-slim AS MAVEN_BUILD

ARG SPRING_ACTIVE_PROFILE

MAINTAINER Umar
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean install -Dspring.profiles.active=$SPRING_ACTIVE_PROFILE && mvn package -B -e -Dspring.profiles.active=$SPRING_ACTIVE_PROFILE
FROM openjdk-17-slim

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/hatim-*.jar /app/hatim.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=$SPRINT_ACTIVE_PROFILE", "-jar", "hatim.jar"]
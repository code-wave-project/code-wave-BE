FROM gradle:8.2.1-jdk17 AS build
WORKDIR /app
COPY . .

ENV GRADLE_USER_HOME /app/.gradle
RUN chmod +x gradlew
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

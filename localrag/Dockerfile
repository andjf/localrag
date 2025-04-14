FROM gradle:8.13-jdk17 AS build
WORKDIR /app

COPY settings.gradle* build.gradle* gradle/ ./
RUN gradle build --no-daemon --dry-run

COPY . .

RUN gradle build --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

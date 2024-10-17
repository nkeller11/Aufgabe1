# Wähle ein Basis-Image
FROM gradle:7.6-jdk17 AS builder

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere die Build-Dateien und das Gradle-Wrapper-Skript
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle/ ./gradle/ 
# Kopiere den Quellcode
COPY src/ ./src/

# Baue die Anwendung (ohne Tests)
RUN chmod +x gradlew && ./gradlew build -x test

# Erstelle das endgültige Image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# Starte die Anwendung
ENTRYPOINT ["java", "-jar", "app.jar"]

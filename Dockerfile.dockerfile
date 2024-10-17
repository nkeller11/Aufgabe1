# Verwende ein Basis-Java-Image
FROM openjdk:17-jdk-slim as builder

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere die build.gradle und die settings.gradle
COPY build.gradle ./

# Kopiere den Quellcode und das Gradle-Wrapper-Skript
COPY gradle gradle
COPY src src

# Baue die Anwendung
RUN ./gradlew build -x test

# Erstelle das endgültige Image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Kopiere die JAR-Datei aus dem Builder-Image
COPY --from=builder /app/build/libs/*.jar app.jar

# Exponiere den Port, den die Anwendung verwendet
EXPOSE 8080

# Starte die Anwendung
CMD ["java", "-jar", "app.jar"]
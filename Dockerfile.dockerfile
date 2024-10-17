# Verwende ein Basis-Java-Image
FROM openjdk:17-jdk-slim

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere die JAR-Datei in das Image
COPY target/your-app-name.jar app.jar

# Exponiere den Port, den die Anwendung verwendet
EXPOSE 8080

# Starte die Anwendung
CMD ["java", "-jar", "app.jar"]

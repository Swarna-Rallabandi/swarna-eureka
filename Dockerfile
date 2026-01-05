FROM eclipse-temurin:17-jre
WORKDIR /app

RUN useradd -m appuser
USER appuser

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
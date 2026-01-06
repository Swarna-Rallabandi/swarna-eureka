FROM eclipse-temurin:17-jre
RUN mkdir -p /opt/i27
WORKDIR /opt/i27
ARG JAR_PATH
COPY ["${JAR_PATH}", "/opt/i27/i27-eureka2.jar"]
EXPOSE 8761
CMD ["java", "-jar", "/opt/i27/i27-eureka2.jar"]


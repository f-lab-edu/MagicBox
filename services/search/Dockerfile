FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu
ARG JAR_FILE=services/search/build/libs/*.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

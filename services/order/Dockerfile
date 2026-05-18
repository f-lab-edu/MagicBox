FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu
ARG JAR_FILE=services/order/build/libs/*.jar
RUN groupadd -r appuser && useradd -r -g appuser appuserWORKDIR /app
COPY ${JAR_FILE} app.jar
RUN chown -R appuser:appuser /app
USER appuserEXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

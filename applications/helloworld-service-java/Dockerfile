FROM openjdk:11.0.6-jre-slim

COPY health-check.sh /app/health-check.sh
RUN chmod +x /app/health-check.sh

COPY target/helloworld-service.jar /app/app.jar

EXPOSE 8080

USER 1001

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

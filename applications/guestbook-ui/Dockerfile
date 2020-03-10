FROM openjdk:11.0.6-jre-slim

COPY health-check.sh /app/health-check.sh
RUN chmod +x /app/health-check.sh

RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates curl netbase && rm -rf /var/lib/apt/lists/*

COPY target/guestbook-ui.jar /app/app.jar

EXPOSE 8080

USER 1001

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

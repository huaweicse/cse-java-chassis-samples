FROM 100.125.0.198:20202/hwcse/dockerhub-java:8-jre-alpine
RUN mkdir -p /home/apps/server
COPY porter-user-service-0.0.1-SNAPSHOT.jar /home/apps/server
WORKDIR /home/apps/server/
ENTRYPOINT ["java", "-Ddb.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}", "-Ddb.username=${DB_USERNAME}", "-Ddb.password=${DB_PASSWD}", "-jar", "porter-user-service-0.0.1-SNAPSHOT.jar"]

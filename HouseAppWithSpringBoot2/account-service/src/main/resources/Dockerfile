FROM 100.125.0.198:20202/hwcse/dockerhub-java:8-jre-alpine
RUN mkdir -p /home/apps/server
COPY ./account-service/target/account-service-0.0.1-SNAPSHOT.jar /home/apps/server
COPY ./account-service/target/lib/ /home/apps/server/lib
ENTRYPOINT ["java", "-Ddb.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}", "-Ddb.username=${DB_USERNAME}", "-Ddb.password=${DB_PASSWD}", "-Dcse.tcc.transaction.redis.host=${TCC_REDIS_HOST}", "-Dcse.tcc.transaction.redis.port=${TCC_REDIS_PORT}", "-Dcse.tcc.transaction.redis.password=${TCC_REDIS_PASSWD}", "-jar", "/home/apps/server/account-service-0.0.1-SNAPSHOT.jar"]

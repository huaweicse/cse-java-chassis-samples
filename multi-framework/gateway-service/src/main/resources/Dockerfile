FROM 100.125.0.198:20202/hwcse/dockerhub-java:8-jre-alpine
RUN mkdir -p /home/apps/server
RUN mkdir -p /home/apps/server/webapp
COPY porter-gateway-service-0.0.1-SNAPSHOT.jar /home/apps/server
COPY ui /home/apps/server/webapp/ui
WORKDIR /home/apps/server/
ENTRYPOINT ["java", "-Dgateway.webroot=webapp", "-jar", "porter-gateway-service-0.0.1-SNAPSHOT.jar"]

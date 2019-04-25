FROM 100.125.0.198:20202/hwcse/dockerhub-java:8-jre-alpine
RUN mkdir -p /home/apps/server
RUN mkdir -p /home/apps/server/webapp
COPY porter-file-service-0.0.1-SNAPSHOT /home/apps/server
WORKDIR /home/apps/server/
ENTRYPOINT ["java", "-jar", "porter-file-service-0.0.1-SNAPSHOT"]

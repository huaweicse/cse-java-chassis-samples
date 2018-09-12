FROM 10.162.197.95:5000/cse-jre:8u111
RUN mkdir -p /home/apps/server
COPY product-service-0.0.1-SNAPSHOT.jar /home/apps/server
COPY lib/ /home/apps/server/lib
ENTRYPOINT ["java", "-jar", "/home/apps/server/product-service-0.0.1-SNAPSHOT.jar"]

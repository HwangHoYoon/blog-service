FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/blog-service-discovery-1.0.jar blog-service-discovery.jar
ENTRYPOINT ["java", "-jar", "blog-service-discovery.jar"]
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/blog-user-service-1.0.jar blog-user-service.jar
ENTRYPOINT ["java", "-jar", "blog-user-service.jar"]
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY apiEncryptionKey.jks apiEncryptionKey.jks
COPY target/blog-config-service-1.0.jar blog-config-service.jar
ENTRYPOINT ["java","-jar","blog-config-service.jar"]
FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/blog-apigateway-service-1.0.jar blog-apigateway-service.jar
ENTRYPOINT ["java","-jar","blog-apigateway-service.jar"]
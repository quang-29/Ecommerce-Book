FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/BookStore.jar BookStore.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "BookStore.jar"]

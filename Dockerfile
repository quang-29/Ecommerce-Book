FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY target/BookStore.jar BookStore.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "BookStore.jar"]

FROM maven:3.9-amazoncorretto-21-alpine AS builder

WORKDIR /jar

COPY . /jar

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine-3.23

WORKDIR /app

COPY --from=builder /jar/target/*.jar ./finance-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "finance-api.jar"]
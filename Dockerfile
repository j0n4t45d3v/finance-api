FROM eclipse-temurin:17-alpine AS build

WORKDIR /app

COPY . /app

CMD ["./gradlew", "clean", "bootJar"]

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /bin/build/libs/*.jar /app/finance-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/finance-api.jar"]
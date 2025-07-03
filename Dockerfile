FROM eclipse-temurin:17-alpine AS build

WORKDIR /build

COPY .. /build

CMD ["./mvnw", "clean", "package"]

FROM eclipse-temurin:17-jre-alpine

WORKDIR /api

COPY --from=build /build/target/*.jar /api/easy-finance-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/api/easy-finance-api.jar"]
#!/usr/bin/env sh

find src/main/java src/main/resources -type f | \
entr -nr ./mvnw compile &

./mvnw spring-boot:run
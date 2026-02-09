#!/usr/bin/env sh
set -e

find src/main/java src/main/resources -type f | \
entr -nr ./mvnw clean compile &

./mvnw spring-boot:run

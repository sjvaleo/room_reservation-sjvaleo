# ---- Build image ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
# Cache deps first
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# Copy sources and build
COPY . .
RUN chmod +x mvnw && ./mvnw -q -DskipTests clean package

# ---- Run image ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /src/target/app.jar /app/app.jar

ENV DB_FILE=/var/tmp/reservation.db

# Render injects $PORT — pass it to Spring Boot
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]

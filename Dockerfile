FROM maven:3.9.8-amazoncorretto-21-debian-bookworm as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -PSimulation

# Second stage: Java runtime
FROM amazoncorretto:21-alpine3.19
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
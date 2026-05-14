FROM ghcr.io/graalvm/jdk-community:25 AS build
RUN microdnf install -y maven && microdnf clean all
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests -q

FROM ghcr.io/graalvm/jdk-community:25
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "--add-opens", "java.base/sun.misc=ALL-UNNAMED", "--add-opens", "java.base/java.nio=ALL-UNNAMED", "-jar", "app.jar"]


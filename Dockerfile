FROM gradle:9.0.0-jdk21 AS BUILD
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=BUILD /app/build/libs/*.jar /app/agendador-tarefas.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/agendador-tarefas.jar"]
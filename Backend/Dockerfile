FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Download the project source from GitHub and build the backend.
RUN git clone https://github.com/Karthickcjx/TodoSummaryAssistant.git .

WORKDIR /app/Backend/todo-summary-assistant
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/Backend/todo-summary-assistant/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

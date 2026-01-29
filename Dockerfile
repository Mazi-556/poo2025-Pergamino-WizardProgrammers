
FROM node:20-alpine as frontend-builder
WORKDIR /app/frontend


COPY frontend/package*.json ./
RUN npm install


COPY frontend/ .

ENV VITE_API_URL="" 
RUN npm run build


FROM maven:3.9.6-eclipse-temurin-21 as backend-builder
WORKDIR /app


COPY pom.xml .
COPY src ./src

COPY --from=frontend-builder /app/frontend/dist ./src/main/resources/static


RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/target/*.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
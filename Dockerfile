FROM openjdk:17-alpine
WORKDIR /app
COPY target/NoteTakingApp*.jar /app/NoteTakingApp.jar
EXPOSE 8080
CMD ["java", "-jar", "NoteTakingApp.jar"]
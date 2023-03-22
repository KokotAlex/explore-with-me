FROM amazoncorretto:11-alpine-jdk
COPY http-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
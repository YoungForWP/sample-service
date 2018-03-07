FROM openjdk:8-jre-slim

EXPOSE 8080

ADD build/libs/sample-service-0.1.0.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
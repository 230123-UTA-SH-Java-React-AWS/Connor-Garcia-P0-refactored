FROM openjdk:8-jre-alpine

COPY target\projectzerospring-0.0.1-SNAPSHOT.jar app.jar

ENV proj0url=$proj0url

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

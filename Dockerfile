#I don't think it's necessary to have this use Java 8, since any compiled
# Java is valid for future Java JVMs without firther modification.
FROM openjdk:8-jre-alpine

COPY target\projectzerospring-0.0.1-SNAPSHOT.jar app.jar

ENV proj0url=$proj0url

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM adoptopenjdk/openjdk11:alpine-slim

RUN mkdir /app

COPY target/ToDo_Application-0.0.1-SNAPSHOT.jar /app/ToDo_Application-0.0.1-SNAPSHOT.jar

WORKDIR /app

CMD ["java", "-jar", "your-application.jar"]
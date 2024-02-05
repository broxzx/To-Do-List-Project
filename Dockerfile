FROM openjdk:17-jdk

WORKDIR /app

COPY target/ToDo_Application-0.0.1-SNAPSHOT.jar /app/ToDo_Application-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "ToDo_Application-0.0.1-SNAPSHOT.jar"]
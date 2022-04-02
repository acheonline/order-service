FROM amazoncorretto:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} identity-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/identity-0.0.1-SNAPSHOT.jar"]
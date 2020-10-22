FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/Mutantes-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} mutantes.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","mutantes.jar"]
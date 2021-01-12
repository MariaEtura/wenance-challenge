FROM maven:alpine as build
USER root
WORKDIR /builder
ADD . /builder
COPY pom.xml pom.xml
RUN mvn clean install

FROM openjdk:8-jre-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=build builder/target/wenance-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "wenance-0.0.1-SNAPSHOT.jar"]
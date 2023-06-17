FROM eclipse-temurin:20.0.1_9-jdk-alpine
MAINTAINER fabriciodpe
COPY target/portfolio-0.0.1-SNAPSHOT.jar portfolio.jar
ENTRYPOINT ["java","-jar","/portfolio.jar"]
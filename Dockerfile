FROM eclipse-temurin:17-jdk-alpine
MAINTAINER fabriciodpe
COPY target/portfolio-0.0.1-SNAPSHOT.jar portfolio.jar
ENTRYPOINT ["java","-jar","/portfolio.jar"]
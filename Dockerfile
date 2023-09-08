FROM openjdk:17-jdk-alpine3.14
ADD target/play-docker-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
CMD java -jar play-docker-0.0.1-SNAPSHOT.jar
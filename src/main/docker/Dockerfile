FROM openjdk:11-jdk-slim

ENV ARTIFACT_FILE authorizationserver-0.2

VOLUME /tmp
ADD maven/$ARTIFACT_FILE.jar $ARTIFACT_FILE.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /$ARTIFACT_FILE.jar
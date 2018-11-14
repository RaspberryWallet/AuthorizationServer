FROM openjdk:11-jdk-slim

ENV ARTIFACT_FILE authorizationserver-0.3
ENV KEY_PASSWORD changeme

RUN mkdir -p /opt/wallet/keys \
    && cd /opt/wallet/keys \
    &&  keytool -genkey -alias authserver -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -storepass \
    $KEY_PASSWORD -validity 3650 -dname "CN=authserver.raspberrywallet.io, OU=Main, O=RasberryWallet, L=Gdansk, S=Pomorskie, C=PL"

ADD maven/$ARTIFACT_FILE.jar $ARTIFACT_FILE.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /$ARTIFACT_FILE.jar
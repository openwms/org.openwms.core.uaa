FROM azul/zulu-openjdk-alpine:11-jre
VOLUME library
ADD target/openwms-core-uaa.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-noverify -XX:+UseSerialGC -Xss512k"
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /app.jar

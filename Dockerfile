FROM java:8-jre
VOLUME library
ADD target/openwms-core-uaa.jar app.jar
RUN bash -c 'touch /app.jar'
ENV JAVA_OPTS="-XX:+UseSerialGC -Xss512k -Dspring.zipkin.enabled=false"
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /app.jar

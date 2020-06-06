FROM adoptopenjdk/openjdk11-openj9:jre-11.0.7_10_openj9-0.20.0-alpine
ARG JAVA_OPTS="-Xshareclasses -Xquickstart -noverify"
ADD target/openwms-core-uaa.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar

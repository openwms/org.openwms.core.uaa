FROM bellsoft/liberica-openjre-alpine:21-cds as builder
MAINTAINER interface21.io <product@openwms.org>
ENV LANG en_GB.UTF-8
WORKDIR application
ARG JAR_FILE=target/openwms-core-uaa-exec.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM bellsoft/liberica-openjre-alpine:21-cds
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT exec java org.springframework.boot.loader.launch.JarLauncher

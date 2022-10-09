FROM openjdk:17-jdk-slim as builder
ARG JAR_FILE=target/employee-service-*.jar
COPY ${JAR_FILE} employee-service.jar
RUN java -Djarmode=layertools -jar employee-service.jar extract

FROM openjdk:17-jdk-slim
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
RUN true
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher"]
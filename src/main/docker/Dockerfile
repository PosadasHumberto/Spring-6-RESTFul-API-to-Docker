FROM openjdk:17-slim as builder
WORKDIR application
ADD target/KBE-Rest-Brewery-0.0.1-SNAPSHOT.jar ./
RUN java -Djarmode=layertools -jar KBE-Rest-Brewery-0.0.1-SNAPSHOT.jar extract

FROM openjdk:17-slim

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]
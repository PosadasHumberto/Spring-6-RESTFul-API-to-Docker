FROM openjdk:17-slim

ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

WORKDIR application

COPY ../../../target/KBE-Rest-Brewery-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-jar", "KBE-Rest-Brewery-0.0.1-SNAPSHOT.jar"]


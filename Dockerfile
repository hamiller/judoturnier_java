# First stage, build the custom JRE
FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS jre-builder

# Install binutils, required by jlink
RUN apk update &&  \
    apk add binutils

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules java.base,java.desktop,java.sql,java.logging,java.xml,java.management,java.naming,java.security.jgss,java.instrument,jdk.crypto.ec,jdk.httpserver,jdk.security.auth,jdk.security.jgss,jdk.crypto.cryptoki \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /optimized-jdk-21

# Second stage, Use the custom JRE and build the app image
FROM alpine:latest

# Fix: Install glibc (benötigt für Java)
RUN apk add --no-cache libc6-compat

# Fix: Korrekte Java-Pfad setzen und testen
ENV JAVA_HOME=/opt/jdk-21
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Fix: Überprüfen, ob JRE korrekt kopiert wurde
COPY --from=jre-builder /optimized-jdk-21 $JAVA_HOME

# Fix: Prüfen, ob `java` ausführbar ist
RUN ls -lah $JAVA_HOME/bin/ && $JAVA_HOME/bin/java -version

COPY ./build/libs/judoturnier.jar judoturnier.jar

ENTRYPOINT ["/opt/jdk-21/bin/java","-jar","/judoturnier.jar"]

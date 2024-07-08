FROM eclipse-temurin:21
MAINTAINER sinnix.de
COPY ./build/libs/judoturnier-0.0.1-SNAPSHOT.war judoturnier.war
ENTRYPOINT ["java","-jar","/judoturnier.war"]
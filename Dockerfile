FROM eclipse-temurin:21
MAINTAINER sinnix.de
COPY ./build/libs/judoturnier.jar judoturnier.jar

ENTRYPOINT ["java","-jar","/judoturnier.jar"]
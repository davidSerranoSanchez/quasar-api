FROM openjdk:8-jdk-alpine

MAINTAINER david_s_s@hotmail.com
VOLUME /tmp
EXPOSE 8080

ADD target/quasar-0.0.1-SNAPSHOT.jar quasar.jar

ENV WAIT_VERSION 2.7.3
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

CMD /wait && java -jar quasar.jar
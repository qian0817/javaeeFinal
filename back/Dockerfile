FROM openjdk:11-slim
MAINTAINER qian0817
COPY build/libs/*.jar start.jar
CMD java -jar -Dspring.profiles.active=prod start.jar
EXPOSE 8080
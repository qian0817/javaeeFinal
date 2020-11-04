FROM openjdk:11
COPY build/libs/*.jar start.jar
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait
CMD /wait && java -jar -Dspring.profiles.active=prod start.jar
EXPOSE 8080
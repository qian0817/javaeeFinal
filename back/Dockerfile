FROM openjdk:11
COPY build/libs/*.jar start.jar
CMD ["java","-jar","-Dspring.profiles.active=prod","start.jar"]
EXPOSE 8080
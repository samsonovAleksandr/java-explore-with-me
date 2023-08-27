FROM amazoncorretto:11
COPY target/*.jar server-stats.jar
ENTRYPOINT ["java","-jar","/server-stats.jar"]
FROM amazoncorretto:11
COPY target/*.jar server-stats.jar
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address*=5005", "-jar","/server-stats.jar"]
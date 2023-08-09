FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAR_FILE} -jar /app.jar"]
FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
EXPOSE 9090
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAR_FILE} -jar /app.jar"]
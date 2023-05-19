FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=build/libs/ea-main-1.1.4.jar
WORKDIR /app
EXPOSE 431
COPY ${JAR_FILE} ea_main.jar
ENTRYPOINT ["java","-jar","ea_main.jar"]
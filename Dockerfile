FROM eclipse-temurin:17

LABEL version="1.0.0"
WORKDIR /app

#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring

EXPOSE 8098

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/barua.jar
#RUN --mount=type=secret,id=secrets \
#    cat /run/secrets/secrets
ENTRYPOINT ["java","-jar","/app/barua.jar"]
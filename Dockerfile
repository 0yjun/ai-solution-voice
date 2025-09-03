# Dockerfile
FROM openjdk:21
WORKDIR /app
COPY build/libs/ai-solution-voice-0.0.1-SNAPSHOT.jar ai-solution-voice-0.0.1-SNAPSHOT.jar
EXPOSE 9094
ENTRYPOINT ["java", "-jar", "ai-solution-voice-0.0.1-SNAPSHOT.jar"]
FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} grid-chat-server.jar
ENTRYPOINT ["java",
"-DclusterName=remote_cluster",
"-DisRemoteTestServer=true",
"-jar","/grid-chat-server.jar"
]
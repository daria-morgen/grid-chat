FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} grid-chat-server.jar
ENTRYPOINT [
"java",
"-DisSuperCluster=true",
"-DpublicIP=194.87.248.33:5701",
"-jar",
"/grid-chat-server.jar"
]
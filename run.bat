java -jar -Dserver.port=8081 ./server/target/server-0.0.1-SNAPSHOT.jar

java -jar -Dserver.port=8082 -DmessageServiceHost=http://localhost:8081 ./client/target/client-0.0.1-SNAPSHOT.jar


java -jar -Dserver.port=8083 ./server/target/server-0.0.1-SNAPSHOT.jar

java -jar -Dserver.port=8084 -DmessageServiceHost=http://localhost:8083 ./client/target/client-0.0.1-SNAPSHOT.jar
# Getting Started

### Documentation


1) Build project
    * clean package
2) Server start
    * java -jar -Dserver.port=8081 server-0.0.1-SNAPSHOT.jar
3) Client start
    * java -jar -Dserver.port=8082 -DmessageServiceHost=http://localhost:8081 client-0.0.1-SNAPSHOT.jar


Download Hazelcast Management Center
https://hazelcast.com/open-source-projects/downloads/
ZIP (115.9 MB)
java -jar hazelcast-management-center-4.2021.06.jar

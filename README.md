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


Для запуска сервера чата на удаленном сервере с чистым IP нужно подключиться к серверу:
194.87.248.33
root
o08z5eZdrP

Перйти в каталог:
cd /home/projects/grid_chat
Выполнить:
java -jar -Dserver.port=8081 -DisRemoteTestServer=true -DclusterName=remote_cluster server-0.0.1-SNAPSHOT.jar
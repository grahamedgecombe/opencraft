@echo off
java -Xms512m -Xmx1024m -cp bin;lib/OpenCommons.jar;lib/mina-core.jar;lib/slf4j-api.jar;lib/slf4j-jdk.jar;lib/xstream-core.jar org.opencraft.server.Server
pause

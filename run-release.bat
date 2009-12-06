@echo off
title OpenCraft
java -Xms512m -Xmx1024m -cp opencraft.jar;mina-core.jar;slf4j-api.jar;slf4j-jdk.jar;xstream-core.jar org.opencraft.server.Server
pause


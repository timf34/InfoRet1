#!/bin/bash

# Ensure script is executable
# chmod +x run.sh

# Clean and compile the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.example.Main"

# Alternatively, package the application and run the jar (if using Maven Shade Plugin)
# mvn clean package
# java -jar target/LuceneCranfieldSearchEngine-1.0-SNAPSHOT.jar

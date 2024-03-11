# Chatop-API

    This projet is the complete back-end api of Chatop Application

## DataBase Installation

    This project is designed to work with MySQL Database.
    You have to :

    - install a MySql Database.
    - create schema for chatop
    - run Script to build tables schema.
    - create a user with read write and delete privileges on the schema
    - adjust configutation in application.properties

## Security

    This API is securized with a jwt token.
    all sensible keys are set and describes in env.sh script

## Development server

    This is a spring boot project that use maven.
    You can build project with maven package goal then run
    on linux system under project root :

       source ./env.sh
       ./mvnw package
       java -jar ./target/chatop-0.0.1-SNAPSHOT.jar

## Build

    Use maven package goal to build this project's executable jar.
    under projet root directory run mvnw package 


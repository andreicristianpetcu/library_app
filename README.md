library_app
===========

Based on the ginkgo-seed archetype.
Scrum board: https://trello.com/b/aNJecyCd/library-app

What you need:
JDK 1.6
Maven 3
Node.js & NPM
Postgresql

How to build:
mvn clean install

How to run:
Start the rest app in tomcat:
rest > mvn tomcat7:run

Start the frontend:
spa > grunt server
OR
spa\app > node ..\scripts\web-server.js

Test your app at:
http://libraryapp.cegeka.com:8000/index.html
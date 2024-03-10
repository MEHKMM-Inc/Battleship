# Project Overview

- The main folder contains all of the files that will run on the client and server.
- The `pom.xml` file adds the dependencies to the server. This tells the server which files are needed to run and where they are located. Do **not** change the folders or path, this exact format is requried.
- `.war` file is the project packaged as a web application that will be read by tomcat

## How to Use

- Download the project `.war` file
- Install tomcat version 10.1.19 or later
- In tomcat, add the `.war ` file as a deployment
- After the deployment has initialized, start the server and wait for tomcat to load
- Open the deployment under tomcat (should be a localhost address)
- Server should now be working and display the webapp

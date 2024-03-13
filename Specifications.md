# Requirement Specifications Document

### The user shall be able to view a battleship grid

A 10 x 10 battleship grid must be viewable and interactable to the user in which the user
can click on each part of the grid seperately

### The user shall be guided where to play next

A combination of colors and text values must be present in each grid square informing the user
on where the next best course of action shall be taken

### The user shall be able to update the gameboard

The user must be able to modify the state of the gameboard through clicking the individual grid
squares, and the board should update accordingly.

The update of the board should happen automatically without any additional input from the user
requesting it to be updated

# Design Specifications Document

### We will create an html website that handles user UI

The website will be a standard html, css, jvs file containing all UI information

### We will create a java program that calculates the values of the board

The java file will preform all calculations necessary for finding the next best move and
will operate at a timely manner

### We will create an Apache Tomcat server that allows the html website and the java program to communicate

- The Tomcat server will be started on the users local machine
- The server will display the html webpage to the user
- User miss, hit, and hit & sunk actions will be read by JQuery and sent to the servlet on the tomcat server
- The server will process the recieved data using the java program
- The servlet will convert the output data to a JSON
- Any data to be sent back will be recieved by JQuery, parsed from a JSON, and then displayed on the html webpage

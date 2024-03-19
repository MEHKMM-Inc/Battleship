# Requirement Specifications Document

### The user shall be able to view a battleship grid

* A 10 x 10 battleship grid must be viewable and interactable to the user in which the user
can click on each part of the grid seperately

* A background image representing battleship will exist and
look clean

### The user shall be guided where to play next

* A text values must be present in each grid square informing the user
on where the next best course of action shall be taken

* After each attack an X or O will be added to represent hit or miss

* Requirement M1: (REPLACE WITH ACTUAL NUMBER IN FINAL DOCUMENT)
	* M1.a: The ProbabilityCalculator class will take in info from the user's last guess
	* M1.b: The ProbabilityCalculator will update the status of the game state to the best of its ability given current info
	* M1.c: The ProbabilityCalculator will calculate the probabilities of landing a hit on each unguessed cell and return them as an array

### The user shall be able to update the game board

* The user must be able to modify the state of the game board through clicking the individual grid
squares, and the board should update accordingly.

* The update of the board should happen automatically without any additional input from the user
requesting it to be updated

### Each space in the game board shall show a percentage

* the players will then use those percentages to make their next move

### The board shall restart after a page reset

* to keep our design simple, we will not have a restart button instead to
  start a new game you must refresh the page

### The system shall use a server to connect back end javascript to the html
- User activates a button on the FE of the project
- JQuery on the users local machine uses an Ajax function to call the server and passes the data associated with the button to it
- On the server, the servlet receives the data and stores it in a local variable(s) depending on the button pressed
- The servlet calls the java algorithm within the server and gets its returned value
- The servlet will then convert the returned data to JSON and return it to the client localhost
- JQuery on the users local machine receives the data and parses it from JSON


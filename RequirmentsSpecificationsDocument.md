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

### The user shall be able to update the game board

* The user must be able to modify the state of the game board through clicking the individual grid
squares, and the board should update accordingly.

* The update of the board should happen automatically without any additional input from the user
requesting it to be updated


## TODO:
* Add how it will suggest
* how marcuses function works
### Server Connection
- User activates a button on the FE of the project
- JQuery on the users local machine uses an Ajax function to call the server and passes the data associated with the button to it
- On the server, the servlet receives the data and stores it in a local variable(s) depending on the button pressed
- The servlet calls the java algorithm within the server and gets its returned value
- The servlet will then convert the returned data to JSON and return it to the client localhost
- JQuery on the users local machine receives the data and parses it from JSON
- FE percentage disaply is updated by javascript based on the received data

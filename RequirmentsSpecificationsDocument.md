# Requirement Specifications Document

### R1. The user shall be able to view a battleship grid

* R1a. A 10 x 10 battleship grid must be viewable and interactable to the user in which the user
can click on each part of the grid seperately

* R1b. A background image representing battleship will exist and
look clean

### R2. The user shall be able to update the game board

* R2a. The user must be able to modify the state of the game board through clicking the individual grid
squares, and the board should update accordingly.

* R2b. The user will be prompted to choose whether the guess was a hit, miss, or sunk

* R2c. If the user selects sunk, they will be promted to select which ship was sunk

* R2d. If the selections is invalid (not enough hits for ship to be sunk, ship already sunk, not possible position it could be in around selected cell) the user will be notified and the app will not call the backend or update the game state.

* R2e. If the input is valid, the frontend board display will update automatically without any additional input from the user.

### R3. The user shall be guided where to play next

* R3a. A text value will be present in each unguessed grid cell informing the user of the chance of landing a hit on that cell.

* R3b. After each attack an X or O will be added to represent hit or miss

* R3c. The calculation algorithm will take in info from the user's last guess
      
* R3d. The algorithm will update the status of the game state to the best of its ability given known info from previous guesses

* R3e. The algorithm will calculate the probabilities of landing a hit on each unguessed cell and return them as an array

### R4. The board shall restart after a page reset

* R4a. The board will reset when the page is refreshed

### R5. The system shall use a server to connect back end javascript to the html
- User activates a button on the FE of the project
- JQuery on the users local machine uses an Ajax function to call the server and passes the data associated with the button to it
- On the server, the servlet receives the data and stores it in a local variable(s) depending on the button pressed
- The servlet calls the java algorithm within the server and gets its returned value
- The servlet will then convert the returned data to JSON and return it to the client localhost
- JQuery on the users local machine receives the data and parses it from JSON
- FE percentage display is updated by javascript based on the received data

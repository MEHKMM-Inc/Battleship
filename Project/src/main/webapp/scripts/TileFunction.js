/*
* Handles the updating and display of the grid tiles
* Works with JQuery to do so
*
* Author: Matthew Mangan
*/

/**
 * By default currentSquare should be null
 * When a grid element is clicked, it is changed to that element object
 * This is global as it is used in multiple functions for display
 */
var currentSquare = null

$(document).ready(function(){

    // Dictionary containing the ships and a identification value 
    var shipTypes = {
        "Cruiser":1,
        "Submarine":2,
        "Destroyer":3,
        "Battleship":4,
        "Aircraft Carrier":5,
    };

    // Dictionary containing ships by size
    var shipSizes = {
        "Destroyer":2,
        "Cruiser":3,
        "Submarine":3,
        "Battleship":4,
        "Aircraft Carrier":5,
    }

    // Dicotionary containing which ships have been sunk
    var hasSunken = {
        "Cruiser":false,
        "Submarine":false,
        "Destroyer":false,
        "Battleship":false,
        "Aircraft Carrier":false,
    }
    // An array of the HTMl elements within the class "grid"
    var gridElements = document.getElementsByClassName("grid");

    // A 2D array containing the grid elements by board layout
    var boardLayout = [];
    for (let i = 0; i < 10; i++) {
        boardLayout.push([]);
    }
    for (let i = 0; i < (gridElements.length / 10); i++) {
        for (let j = 0; j < (gridElements.length / 10); j++) {
            boardLayout[i].push(gridElements[j + (i*10)]);
        }
    }
    /**
     * Takes an integer index value and returns all adjacent squares within the grid.
     * @param {Integer} index The location of the square on the grid
     * @returns All adjacent squares based on the grid element at index
     */
    function getAdjacent(index) {
        let surrounding = [];
        const column = boardLayout.length
        const row = boardLayout[0].length

        const leftEdge = index % column === 0;
        const rightEdge = (index + 1) % column === 0;
        const topEdge = Math.floor(index / column) === 0;
        const bottomEdge = Math.floor(index / column) === (row - 1);

        // Check edge cases
        if (!leftEdge) {
            surrounding.push(index - 1);
        }
        if (!rightEdge) {
            surrounding.push(index + 1);
        }
        if (!topEdge) {
            surrounding.push(index - column);
        }
        if (!bottomEdge) {
            surrounding.push(index + column);
        }

        return surrounding;
    }

    /**
     * Returns the index of a element within the battleship grid
     * @param {HTMLElement} element An element to locate within the grid
     * @returns The index of the element in the grid
     */
    function getIndex(element) {
        // Index is the location in the grid of the element
        var index = 0;
        for (var i = 0; i < gridElements.length; i++) {
            // If it's found, break the loop as that is the location
            if (gridElements[i] != element) {
                index++;
            } else {
                return index;
            }
        }
        return index;
    }

    /**
     * Sets the textnode values within the grid elements to the percentages held by cellProbabilities
     * @param {Int16Array} cellProbabilities Integer array containing the values displayed on the board
     */
    function setPercentage(cellProbabilities) {

        for (let i = 0; i < cellProbabilities.length; i++) {
            for (let j = 0; j < cellProbabilities.length; j++) {
                // Creates a textnode with the current cellProbabilities value
                var textnode = document.createTextNode(Math.round((cellProbabilities[i][j] + Number.EPSILON) * 100) + "%");

                var gridElement = gridElements[j + (i*10)];
                
                // If there is already a child, change its value
                // Otherwise attach the textnode to the grid element
                if (gridElement.hasChildNodes()) {
                    if (gridElement.classList.contains("miss") || gridElement.classList.contains("hit")) {
                        gridElement.firstChild.nodeValue = "";
                    } else {
                        gridElement.firstChild.nodeValue = (textnode.nodeValue);
                    }
                } else {
                    gridElement.appendChild(textnode);
                }
            }
        }
    }
    /**
     * Calculates the X and Y coordinates of the given element
     * @param {HTMLElement} element An HTML element within the class "grid"
     */
    function getCoordinates(element) {
        var index = getIndex(element);
        // Set Y to 0 and X to either the x-axis or 0
        var x, y = 0;
        if (index >= 10) {
            x = Math.floor(index / 10);
        }
        else {
            x = 0;
        }
        y = (index - (x * 10));
        return [x, y];
    }

    /**
     * Calculates if there is a next ship that has been hit
     * If there is, continues until either conditions cannot be met or until ship is sunk is valid
     * @param {Integer} shipSize The size of the ship
     * @param {Integer} offset The location of the next grid element
     * @param {Integer} x The X coordinate of the current element
     * @param {Integer} y The Y coordinate of the current element
     * @returns The decremented ship size
     */
    function findNextSquare(shipSize, offset, x, y) {
        // If y is 0, move along the x axis
        if (y == 0) {
            if (offset + x > 100 || offset - x < 0) {
                return;
            }
        // Otherwise move along the y axis
        } else {
            if (offset + y > 100 || offset - y < 0) {
                return;
            }
        }
        var overflow = Math.floor(offset / boardLayout[0].length) * boardLayout[0].length;
        var rowOverflow = overflow + boardLayout[0].length;
        // If ship size is greater than 1 and grid element is within bound, continue to find new squares
        if (shipSize > 1) {
            if (offset + x > overflow && offset + x <= rowOverflow) {
                if (gridElements[offset + x + y].classList.contains("hit")) {
                    shipSize--;
                    findNextSquare(shipSize, (offset + x + y), x, y);
                }
            }
        }
        return shipSize;
    }

    /**
     * Determines if a ship can be sunk based on the current hit squares
     * @param {Integer} shipSize The size of the ship
     * @param {String} shipType The name of the ship  
     * @param {HTMLElement} current The current selected HTML element
     */
    function canSink(shipSize, shipType, current) {
        var index = getIndex(current);
        var surrounding = getAdjacent(index);

        // If the ship is already sunken, cannot be again
        if (hasSunken[shipType]) {
            return;
        }
        // Check each adjacent square
        for (let i = 0; i < surrounding.length; i++) {
            var gridElement = gridElements[surrounding[i]];
            // If the square has been hit check the rest the other squares
            if (gridElement.classList.contains("hit")) {
                var offset = (getIndex(gridElement) - index);
                // If offset is great than or less than 1, must be on y axis
                if (offset > 1 || offset < -1) {
                    shipSize = findNextSquare(shipSize, index, 0, offset);
                    if (shipSize > 1) {
                        shipSize = findNextSquare(shipSize, index, 0, offset);
                    }
                    // If the offset is only 1 or -1, must be on x axis
                } else if (offset == 1 || offset == -1) {
                    shipSize = findNextSquare(shipSize, index, offset, 0);
                    if (shipSize > 1) {
                        shipSize = findNextSquare(shipSize, index, offset, 0);
                    }
                }
                // If the ship size is reduced to 1, it is sunken
                // It is left at 1 because it accounts for the original square that hit and sunk was activated on
                if (shipSize = 1) {
                    hasSunken[shipType] = true;
                    alert("Ship Sunk: " + shipType);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Fades an HTML element out
     * @param {HTMLElement} element An HTML element that will be faded out 
     */
    function fade(element) {
        var opacity = 1;
        var timer = setInterval(function () {
            if (opacity <= 0.1) {
                clearInterval(timer);
                $(element).css("visibility", "hidden");
            }
            $(element).css("opacity", opacity); 
            $(element).css("filter", "alpha(opacity=" + opacity * 100 + ")");
            opacity -= opacity * 0.1;
        }, 5);
    }

    /**
     * Fades an HTMl element in
     * @param {HTMLElement} element An HTML element that will be faded in
     */
    function fadeIn(element) {
        var opacity = 0.1;
        $(element).css("visibility", "visible");
        var timer = setInterval(function () {
            if (opacity >= 1) {
                clearInterval(timer);
            }
            $(element).css("opacity", opacity); 
            $(element).css("filter", "alpha(opacity=" + opacity * 100 + ")");
            opacity += opacity * 0.1;
        }, 5);
    }

    /**
     * Handles the event of a miss action
     * Default button actions are disabled to prevent page refresh
     * Using an ajax call, gets the updated percentage values from the server and displays them
     */
    $("#miss_submit").click(function(button){
        button.preventDefault()
        var [x, y] = getCoordinates(currentSquare);
        var select = document.getElementById("select");
        $(currentSquare).css("background-color", "white");
        $(currentSquare).off("click");
        $(currentSquare).off("mouseover");
        $(currentSquare).off("mouseleave");
        currentSquare.classList.add("miss");
        $.ajax({
            url : "./api",
            // Data sent to the server: left is variable name and right is value
            data : {
                hit: "false",
                x: x,
                y: y,
                shipType: 0
            },
            // "get" because we are receiving data from the server
            type : "get",
            // On success, update the front end
            success : function(response) {
                var cellProbabilities = JSON.parse(response)
                setPercentage(cellProbabilities);                
            },
            // On error, alert the user
            error: function() {
                alert("error");
            }
        });
        fade(select);
    });
    /**
     * Handles the event of a hit action
     * Default button actions are disabled to prevent page refresh
     * Using an ajax call, gets the updated percentage values from the server and displays them
     */
    $("#hit_submit").click(function(button){
        button.preventDefault();
        var [x, y] = getCoordinates(currentSquare);
        var select = document.getElementById("select");
        $(currentSquare).css("background-color", "red");
        $(currentSquare).off("click");
        $(currentSquare).off("mouseover");
        $(currentSquare).off("mouseleave");
        currentSquare.classList.add("hit");
        $.ajax({
            url : "./api",
            // Data sent to the server: left is variable name and right is value
            data : {
                hit: "true",
                x: x,
                y: y,
                shipType: 0
            },
            // "get" because we are receiving data from the server
            type : "get",
            // On success, update the front end
            success : function(response) {
                var cellProbabilities = JSON.parse(response)
                setPercentage(cellProbabilities);
            },
            // On error, alert the user
            error: function() {
                alert("error");
            }
        });
        fade(select);
    });
    /**
     * Handles the event of a hit and sunk action
     * Default button actions are disabled to prevent page refresh
     * Unique from the other buttons, this opens a second dialog box prompting for the ship type
     */
    $("#hit_and_sunk_submit").click(function(button){
        button.preventDefault();
        var select = document.getElementById("select");
        var shipSunk = document.getElementById("shipType");
        $(select).css("opacity", 0);
        $(select).css("visibility", "hidden");
        $(shipSunk).css("opacity", 1);
        $(shipSunk).css("visibility", "visible");
    });

    /**
     * For each button in sunkButtons, attach a click event
     * Offset by 1 in the data sent due to 0 resulting in a failed sunk event in the previous buttons
     * Default button actions are disabled to prevent page refresh
     */
    document.querySelectorAll(".sunk_button").forEach((element) => {
        $(element).click(function(button) {
            button.preventDefault();
            var [x, y] = getCoordinates(currentSquare);
            var shipSunk = document.getElementById("shipType");

            if (canSink(shipSizes[element.textContent], element.textContent, currentSquare)) {
                $(currentSquare).css("background-color", "red");
                $(currentSquare).off("click");
                $(currentSquare).off("mouseover");
                $(currentSquare).off("mouseleave");
                currentSquare.classList.add("hit");
                $.ajax({
                    url : "./api",
                    // shipType is sending "i" to the server because the buttons are displayed
                    // in the same order as the java enum class is written
                    data : {
                        hit: "true",
                        x: x,
                        y: y,
                        shipType: shipTypes[element.textContent]
                    },
                    type : "get",
                    success : function(response) {
                        var cellProbabilities = JSON.parse(response)
                        setPercentage(cellProbabilities);
                    },
                    error: function() {
                        alert("error");
                    }
                });
            } else {
                if (hasSunken[element.textContent]) {
                    alert("You have already sunken this ship!");
                } else {
                    alert("failed");
                }
            }
            fade(shipSunk);
        });
    });
    
    /**
     * When the "selection" cancel option is selected, hides the dialog box
     */
    $("#cancel_submit").click(function(button){
        button.preventDefault();
        var select = document.getElementById("select");
        fade(select);
    });
    /**
     * When the "hit and sunk" cancel option is selected, hides the dialog box
     */
    $("#cancel_sunk_submit").click(function(button){
        button.preventDefault();
        var shipSunk = document.getElementById("shipType");
        fade(shipSunk);
    });
    /**
     * For every element in the class "grid", this attaches an "on click" event
     * The event triggers the form dialog box to appear
     * currentSquare is set to the clicked object
     */
    $(".grid").on("click", function() {
        var select = document.getElementById("select");
        var header = document.getElementById("select_header");
        $(header).html(this.id);
        fadeIn(select);
        currentSquare = document.getElementById(this.id);
    });

    /**
     * Do a default call to the server to get initial values
     */
    $.ajax({
        url : "./api",
        // Data sent to the server: left is variable name and right is value
        data : {
            hit: "false",
            x: -1,
            y: -1,
            shipType: 0
        },
        // "get" because we are receiving data from the server
        type : "get",
        // On success, update the front end
        success : function(response) {
            var cellProbabilities = JSON.parse(response)
            setPercentage(cellProbabilities);
        },
        // On error, alert the user
        error: function() {
            alert("error");
        }
    });

    /**
     * Adds a hover event to all grid elements
     * This can be changed to an image or different color later
     */
    document.querySelectorAll(".grid").forEach((element) => {
        $(element).on("mouseover", function() {
            // Change this line to change hover display
            $(element).css("background-color", "blue");
        });

        $(element).on("mouseleave", function() {
            // Change this line to change hover display
            $(element).css("background-color", "transparent");
        });
    });
});
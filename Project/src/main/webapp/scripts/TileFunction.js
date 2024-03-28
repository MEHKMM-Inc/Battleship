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

    // An array of the buttons used when "hit and sunk" is clicked
    var sunkButtons = document.getElementsByClassName("sunk_button");
    // An array of the HTMl elements within the class "grid"
    var gridElements = document.getElementsByClassName("grid");

    /**
     * Sets the textnode values within the grid elements to the percentages held by cellProbabilities
     * @param {Int16Array} cellProbabilities Integer array containing the values displayed on the board
     */
    function setPercentage(cellProbabilities) {

        for (let i = 0; i < cellProbabilities.length; i++) {
            for (let j = 0; j < cellProbabilities.length; j++) {
                // Creates a textnode with the current cellProbabilities value
                var textnode = document.createTextNode(cellProbabilities[i][j] + "%");

                var gridElement = gridElements[j + (i*10)];
                
                // If there is already a child, change its value
                // Otherwise attach the textnode to the grid element
                if (gridElement.firstChild) {
                    gridElement.firstChild.nodeValue = (textnode.nodeValue);
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
        // Index is the location in the grid of the element
        var index = 0;
        for (var i = 0; i < gridElements.length; i++) {
            // If it's found, break the loop as that is the location
            if (gridElements[i] != element) {
                index++;
            } else {
                break;
            }
        }
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
     * Handles the event of a miss action
     * Default button actions are disabled to prevent page refresh
     * Using an ajax call, gets the updated percentage values from the server and displays them
     */
    $("#miss_submit").click(function(button){
        button.preventDefault()
        var [x, y] = getCoordinates(currentSquare);
        $(currentSquare).css("background-color", "red");
        $(currentSquare).off("click");
        $(currentSquare).off("mouseover");
        $(currentSquare).off("mouseleave");
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
                var select = document.getElementById("select");
                setPercentage(cellProbabilities);
                $(select).css("visibility", "hidden");
                
            },
            // On error, alert the user
            error: function() {
                alert("error");
            }
        });
    });
    /**
     * Handles the event of a hit action
     * Default button actions are disabled to prevent page refresh
     * Using an ajax call, gets the updated percentage values from the server and displays them
     */
    $("#hit_submit").click(function(button){
        button.preventDefault();
        var [x, y] = getCoordinates(currentSquare);
        $(currentSquare).css("background-color", "green");
        $(currentSquare).off("click");
        $(currentSquare).off("mouseover");
        $(currentSquare).off("mouseleave");
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
                var select = document.getElementById("select");
                setPercentage(cellProbabilities);
                $(select).css("visibility", "hidden");
            },
            // On error, alert the user
            error: function() {
                alert("error");
            }
        });
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
        $(select).css("visibility", "hidden");
        $(shipSunk).css("visibility", "visible");
    });

    /**
     * For each button in sunkButtons, attach a click event
     * Offset by 1 in the data sent due to 0 resulting in a failed sunk event in the previous buttons
     * Default button actions are disabled to prevent page refresh
     */
    for (var i = 0; i < (sunkButtons.length); i++) {
        $(sunkButtons[i]).click(function(button) {
            button.preventDefault();
            var [x, y] = getCoordinates(currentSquare);
            $(currentSquare).css("background-color", "green");
            $(currentSquare).off("click");
            $(currentSquare).off("mouseover");
            $(currentSquare).off("mouseleave");
            $.ajax({
                url : "./api",
                // shipType is sending "i" to the server because the buttons are displayed
                // in the same order as the java enum class is written
                data : {
                    hit: "true",
                    x: x,
                    y: y,
                    shipType: (i + 1)
                },
                type : "get",
                success : function(response) {
                    var cellProbabilities = JSON.parse(response)
                    var shipSunk = document.getElementById("shipType");
                    setPercentage(cellProbabilities);
                    $(shipSunk).css("visibility", "hidden");
                },
                error: function() {
                    alert("error");
                }
            });
        });
    }
    
    /**
     * When the "selection" cancel option is selected, hides the dialog box
     */
    $("#cancel_submit").click(function(button){
        button.preventDefault();
        var select = document.getElementById("select");
        $(select).css("visibility", "hidden");
    });
    /**
     * When the "hit and sunk" cancel option is selected, hides the dialog box
     */
    $("#cancel_sunk_submit").click(function(button){
        button.preventDefault();
        var shipSunk = document.getElementById("shipType");
        $(shipSunk).css("visibility", "hidden");
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
        $(select).css("visibility", "visible");
        currentSquare = document.getElementById(this.id);
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
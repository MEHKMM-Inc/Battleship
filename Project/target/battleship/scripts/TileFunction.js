/*
* Handles the updating and display of the grid tiles
* Works with JQuery to do so
*/
var currentSquare = null

$(document).ready(function(){

    var sunkButtons = document.getElementsByClassName("sunk_button");

    function setPercentage(cellProbabilities) {
        var gridElements = document.getElementsByClassName("grid");

        for (let i = 0; i < cellProbabilities.length; i++) {
            for (let j = 0; j < cellProbabilities.length; j++) {
                var textnode = document.createTextNode(cellProbabilities[i][j] + "%");

                var gridElement = gridElements[j + (i*10)];
                
                if (gridElement.firstChild) {
                    gridElement.firstChild.nodeValue = (textnode.nodeValue);
                } else {
                    gridElement.appendChild(textnode);
                }
            }
        }
    }
    // Miss button on the dialog pop up
    // Posts to the servlet that the user has missed
    $("#miss_submit").click(function(button){
        button.preventDefault();
        $(currentSquare).css("background-color", "red");
        $.ajax({
            url : "./api",
            data : {
                hit: "false",
                sunk: "false"
            },
            type : "get",
            success : function(response) {
                //$("#testAPI").text(response);
                var cellProbabilities = JSON.parse(response)
                for (let i = 0; i < cellProbabilities.length; i++) {
                    for (let j = 0; j < cellProbabilities.length; j++) {
                        console.log(cellProbabilities[i][j]);
                    }
                }
                var select = document.getElementById("select");
                setPercentage(cellProbabilities);
                $(select).css("visibility", "hidden");
                
            },
            error: function() {
                alert("error");
            }
        });
    });
    // Hit button on the dialog pop up
    // Posts to the servlet that the user has hit
    $("#hit_submit").click(function(button){
        button.preventDefault();
        $(currentSquare).css("background-color", "green");
        $.ajax({
            url : "./api",
            data : {
                hit: "true",
                sunk: "false"
            },
            type : "get",
            success : function(response) {
                //$("#testAPI").text(response);
                var cellProbabilities = JSON.parse(response)
                for (let i = 0; i < cellProbabilities.length; i++) {
                    for (let j = 0; j < cellProbabilities.length; j++) {
                        console.log(cellProbabilities[i][j]);
                    }
                }
                var select = document.getElementById("select");
                setPercentage(cellProbabilities);
                $(select).css("visibility", "hidden");
            },
            error: function() {
                alert("error");
            }
        });
    });
    // Hit & Miss button on the dialog pop up
    // Posts to the servlet that the user has hit and sunk a ship
    $("#hit_and_sunk_submit").click(function(button){
        button.preventDefault();
        var select = document.getElementById("select");
        var shipSunk = document.getElementById("shipType");
        $(select).css("visibility", "hidden");
        $(shipSunk).css("visibility", "visible");
    });

    for (var i = 0; i < sunkButtons.length; i++) {
        $(sunkButtons[i]).click(function(button) {
            button.preventDefault();
            $(currentSquare).css("background-color", "green");
            $.ajax({
                url : "./api",
                data : {
                    hit: "true",
                    sunk: "true",
                    shipType: i
                },
                type : "get",
                success : function(response) {
                    //$("#testAPI").text(response);
                    var cellProbabilities = JSON.parse(response)
                    for (let i = 0; i < cellProbabilities.length; i++) {
                        for (let j = 0; j < cellProbabilities.length; j++) {
                            console.log(cellProbabilities[i][j]);
                        }
                    }
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
    
    // Cancel button on the dialog pop up
    // Closes the dialog pop up as its only function
    $("#cancel_submit").click(function(button){
        button.preventDefault();
        var select = document.getElementById("select");
        $(select).css("visibility", "hidden");
    });
    // Cancel button on the dialog pop up
    // Closes the dialog pop up as its only function
    $("#cancel_sunk_submit").click(function(button){
        button.preventDefault();
        var shipSunk = document.getElementById("shipType");
        $(shipSunk).css("visibility", "hidden");
    });
    // Every element of the grid class will trigger the dialog pop up
    // The selected tile will also display its name on the dialog box
    $(".grid").on("click", function() {
        var select = document.getElementById("select");
        var header = document.getElementById("select_header");
        $(header).html(this.id);
        $(select).css("visibility", "visible");
        currentSquare = document.getElementById(this.id);
    });
});

function mouseOver(id) {
    var getId = document.getElementById(id);
    $(getId).css("background-color", "blue");
}
function mouseOut(id) {
    var getId = document.getElementById(id);
    $(getId).css("background-color", "#52bee9");
}
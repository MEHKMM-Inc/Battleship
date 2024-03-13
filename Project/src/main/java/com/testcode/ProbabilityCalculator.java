package com.testcode;
// Probability Calculator Algorithm
// Marcus King

public class ProbabilityCalculator
{

    // keep track of which boats are sunk
    private Boolean sunkDestroyer = false, sunkCruiser = false, sunkSub = false, 
    sunkBattleship = false, sunkCarrier = false;

    // 2d array of cellStatus values for each cell in the grid
    private cellStatus[][] statusArray = new cellStatus[10][10];

    // 2d array of probabilities corresponding to each cell in the grid
    private double cellProbabilityArray[][] = new double[10][10];

    // keep track of number of hits and misses for probability calculations later
    private int numHits = 0, numMisses = 0;

    public ProbabilityCalculator()
    {
        // initialize statusArray 
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                statusArray[i][j] = cellStatus.blank;
            }
        }
    }

    private void updateSunk(int x, int y, shipSunk typeSunk)
    {
        boolean left = false, right  = false, up = false, down = false;
        int possiblePositions = 0;

        statusArray[x][y] = cellStatus.sunk; // we know this cell is sunk
                
        switch(typeSunk)
        {
            case destroyer:
            {
                sunkDestroyer = true;
                if(numHits == 2)
                {
                    numHits = 0;
                    //convert all hits to sunk
                    for(int i = 0; i < 10; i++)
                    {
                        for(int j = 0; j < 10; j++)
                        {
                            if (statusArray[i][j] == cellStatus.hit)
                            {
                                statusArray[i][j] = cellStatus.sunk;
                            }
                        }
                    }
                }
                else // check positions around hit for possible sunk ship. 
                    //If only one possible position, update status of those cells to sunk
                {
                    if(x - 1 >= 0)
                    {
                        if(statusArray[x - 1][y] == cellStatus.hit)
                        {
                            left = true; // could be to the left
                            possiblePositions++;
                        }
                    }
                    if(x + 1 <= 9)
                    {
                        if(statusArray[x + 1][y] == cellStatus.hit)
                        {
                        right = true; // could be to the right
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2)
                    {
                        if(y - 1 >= 0)
                        {
                            if(statusArray[x][y - 1] == cellStatus.hit)
                            {
                                up = true; // could be up
                                possiblePositions++;
                            }

                            if(possiblePositions < 2)
                            {
                                if(y + 1 <= 9)
                                {
                                    if(statusArray[x][y + 1] == cellStatus.hit)
                                    {
                                        down = true; // could be down
                                        possiblePositions++;
                                    }
                                }
                            }
                        }
                    }
                            
                    if(possiblePositions == 1) // update status to sunk
                    {
                        numHits--;
                        if(left == true)
                        {
                            statusArray[x - 1][y] = cellStatus.sunk;
                        }
                        else if(right == true)
                        {
                            statusArray[x + 1][y] = cellStatus.sunk;
                        }
                        else if(up == true)
                        {
                            statusArray[x][y - 1] = cellStatus.sunk;
                        }
                        else if(down == true)
                        {
                            statusArray[x][y + 1] = cellStatus.sunk;
                        }
                    }

                }
                break;
            }
            case cruiser:
            {
                sunkCruiser = true;
                break;
            }
            case sub:
            {
                sunkSub = true;
                break;
            }
            case battleship:
            {
                sunkBattleship = true;
                break;
            }
            case carrier:
            {
                sunkCarrier = true;
                break;
            }
            default:
            {
                break;
            }
        }     
    }

    // takes statuse of cell just guessed (should be hit or miss), x/y coordinates of guess,
    // and type of ship sunk (none, destroyer, cruiser, sub, battleship, carrier)
    public double[][] calculate(Boolean hit, int x, int y, shipSunk typeSunk)
    {

        // Update game state from new info
        if(hit == false) // guess was a miss
        {
            numMisses++;
        }
        else // guess was a hit
        {
            numHits++;

            if(typeSunk == shipSunk.none) // a ship was not sunk
            {
                statusArray[x][y] = cellStatus.hit; // update cellStatus to hit
            }
            else // a ship was sunk
            {
                updateSunk(x, y, typeSunk); // call function to convert hits to sunk
            }
            
        }

        // Calculate Probabilities

        // will call private calculate function that does the following: (will also call during initialization)

        // for every cell in grid
            // for every unsunk ship
                // check left, right, up, down for possible positions
                // +1 point per possible position (no misses or sunk), +1 per position with existing hits
            //cell probability = points / (100 - total hits - total sunk - total misses)

        return cellProbabilityArray;
    }



}
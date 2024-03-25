package Java;

// Probability Calculator Algorithm
// Marcus King



public class ProbabilityCalculator
{

    // keep track of which boats are sunk
    private Boolean sunkDestroyer = false, sunkCruiser = false, sunkSub = false, 
    sunkBattleship = false, sunkCarrier = false;

    // x,y cooridinates of "ghost ships": ships which are sunk but exact location is unknown
    Coordinate ghostDestroyer = new Coordinate(); 
    Coordinate ghostCruiser = new Coordinate(); 
    Coordinate ghostSub = new Coordinate(); 
    Coordinate ghostBattleship = new Coordinate(); 
    Coordinate ghostCarrier = new Coordinate(); 

    // 2d array of cellStatus values for each cell in the grid
    private cellStatus[][] statusArray = new cellStatus[10][10];

    // 2d array of probabilities corresponding to each cell in the grid
    private double cellProbabilityArray[][] = new double[10][10];

    // keep track of number of hits and misses for probability calculations later
    private int numHits = 0, numMisses = 0, numSunk = 0;

    public ProbabilityCalculator() // call this on program start
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

    // takes statuse of cell just guessed (should be hit or miss), x/y coordinates of guess,
    // and type of ship sunk (none, destroyer, cruiser, sub, battleship, carrier)
    public double[][] calculate(Boolean hit, int x, int y, shipSunk typeSunk)
    {

        // Update game state from new info
        if(hit == false) // guess was a miss
        {
            numMisses++;
            statusArray[x][y] = cellStatus.miss;
        }
        else // guess was a hit
        {
            
            if(typeSunk == shipSunk.none) // a ship was not sunk
            {
                numHits++;
                statusArray[x][y] = cellStatus.hit; // update cellStatus to hit
            }
            else // a ship was sunk
            {
                numSunk += 1;
                switch(typeSunk)
                {
                    case destroyer:
                    {
                        ghostDestroyer.set(x, y);
                        break;
                    }
                    case cruiser:
                    {
                        ghostCruiser.set(x, y);
                        break;
                    }
                    case sub:
                    {
                        ghostSub.set(x, y);
                        break;
                    }
                    case battleship:
                    {
                        ghostBattleship.set(x, y);
                        break;
                    }
                    case carrier:
                    {
                        ghostCarrier.set(x, y);
                        break;
                    }
                }
            }

            // for each "ghost ship" (sunk but not found)

            // updateSunk(ghostx, ghosty, typeSunk)
            if(ghostDestroyer.x > -1 && ghostDestroyer.y > -1)
            {
                updateSunk(ghostDestroyer.x, ghostDestroyer.y, shipSunk.destroyer); // call function to convert hits to sunk
            }
            if(ghostCruiser.x > -1 && ghostCruiser.y > -1)
            {
                updateSunk(ghostCruiser.x, ghostCruiser.y, shipSunk.cruiser); // call function to convert hits to sunk
            }
            if(ghostSub.x > -1 && ghostSub.y > -1)
            {
                updateSunk(ghostSub.x, ghostSub.y, shipSunk.sub); // call function to convert hits to sunk
            }
            if(ghostBattleship.x > -1 && ghostBattleship.y > -1)
            {
                updateSunk(ghostBattleship.x, ghostBattleship.y, shipSunk.battleship); // call function to convert hits to sunk
            }
            if(ghostCarrier.x > -1 && ghostCarrier.y > -1)
            {
                updateSunk(ghostCarrier.x, ghostCarrier.y, shipSunk.carrier); // call function to convert hits to sunk
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

    // this function tries to narrow down the exact location of a sunk ship
    private void updateSunk(int x, int y, shipSunk typeSunk)
    {
        boolean left = false, right  = false, up = false, down = false, 
        middleHorizontal = false, middleLeft = false, middleRight = false, 
        middleVerticle = false, middleUp = false, middleDown = false;
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
                    numSunk += 1;
                    ghostDestroyer.set(-1, -1);
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
                        numSunk++;
                        ghostDestroyer.set(-1, -1);
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
            case sub: // sub and cruiser are same length so calculations are the same
            {
                sunkSub = true;
            }
            case cruiser:
            {
                if(typeSunk == shipSunk.cruiser)
                {
                    sunkCruiser = true;
                }
                
                if(numHits == 3)
                {
                    numHits = 0;
                    numSunk += 2;
                    if(typeSunk == shipSunk.cruiser)
                    {
                        ghostCruiser.set(-1, -1);
                    }
                    else
                    {
                        ghostSub.set(-1, -1);
                    }
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
                    if(x - 2 >= 0)
                    {
                        if(statusArray[x - 2][y] == cellStatus.hit && statusArray[x - 1][y] == cellStatus.hit)
                        {
                            left = true; // could be to the left
                            possiblePositions++;
                        }
                    }
                    if(x + 2 <= 9)
                    {
                        if(statusArray[x + 2][y] == cellStatus.hit && statusArray[x + 1][y] == cellStatus.hit)
                        {
                        right = true; // could be to the right
                            possiblePositions++;
                        }
                    }
                    if(statusArray[x + 1][y] == cellStatus.hit && statusArray[x - 1][y] == cellStatus.hit)
                    {
                        middleHorizontal = true; // could be in the middle
                        possiblePositions++;
                    }
                    if(possiblePositions < 2)
                    {
                        if(y - 2 >= 0)
                        {
                            if(statusArray[x][y - 2] == cellStatus.hit && statusArray[x][y - 1] == cellStatus.hit)
                            {
                                up = true; // could be up
                                possiblePositions++;
                            }

                            if(possiblePositions < 2)
                            {
                                if(y + 2 <= 9)
                                {
                                    if(statusArray[x][y + 2] == cellStatus.hit && statusArray[x][y + 1] == cellStatus.hit)
                                    {
                                        down = true; // could be down
                                        possiblePositions++;
                                    }
                                }
                            }
                            if(possiblePositions < 2)
                            {
                                if(statusArray[x][y+1] == cellStatus.hit && statusArray[x][y-1] == cellStatus.hit)
                                {
                                    middleVerticle = true; // could be in the middle
                                    possiblePositions++;
                                }
                            }
                        }
                    }
                            
                    if(possiblePositions == 1) // update status to sunk
                    {
                        numHits-=2;
                        numSunk+=2;
                        if(typeSunk == shipSunk.cruiser)
                        {
                            ghostCruiser.set(-1, -1);
                        }
                        else
                        {
                            ghostSub.set(-1, -1);
                        }
                        
                        if(left == true)
                        {
                            statusArray[x - 1][y] = cellStatus.sunk;
                            statusArray[x - 2][y] = cellStatus.sunk;
                        }
                        else if(right == true)
                        {
                            statusArray[x + 1][y] = cellStatus.sunk;
                            statusArray[x + 2][y] = cellStatus.sunk;
                        }
                        else if(middleHorizontal == true)
                        {
                            statusArray[x + 1][y] = cellStatus.sunk;
                            statusArray[x - 1][y] = cellStatus.sunk;
                        }
                        else if(up == true)
                        {
                            statusArray[x][y - 1] = cellStatus.sunk;
                            statusArray[x][y - 2] = cellStatus.sunk;
                        }
                        else if(down == true)
                        {
                            statusArray[x][y + 1] = cellStatus.sunk;
                            statusArray[x][y + 2] = cellStatus.sunk;
                        }
                        else if(middleVerticle == true)
                        {
                            statusArray[x][y + 1] = cellStatus.sunk;
                            statusArray[x][y - 1] = cellStatus.sunk;
                        }
                    }
                }
                break;
            }
            case battleship:
            {
                sunkBattleship = true;
                if(numHits == 4)
                {
                    numHits = 0;
                    numSunk += 3;
                    ghostBattleship.set(-1, -1);
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
                    if(x - 3 >= 0)
                    {
                        if(statusArray[x - 3][y] == cellStatus.hit && statusArray[x - 2][y] == cellStatus.hit && statusArray[x - 1][y] == cellStatus.hit)
                        {
                            left = true; // could be to the left
                            possiblePositions++;
                        }
                    }
                    if(x + 3 <= 9)
                    {
                        if(statusArray[x + 3][y] == cellStatus.hit && statusArray[x + 2][y] == cellStatus.hit && statusArray[x + 1][y] == cellStatus.hit)
                        {
                        right = true; // could be to the right
                            possiblePositions++;
                        }
                    }
                    if(statusArray[x + 1][y] == cellStatus.hit && statusArray[x - 1][y] == cellStatus.hit && statusArray[x - 2][y] == cellStatus.hit)
                    {
                        middleLeft = true; // could be in the middle left
                        possiblePositions++;
                    }
                    if(statusArray[x - 1][y] == cellStatus.hit && statusArray[x + 1][y] == cellStatus.hit && statusArray[x + 2][y] == cellStatus.hit)
                    {
                        middleRight = true; // could be in the middle right
                        possiblePositions++;
                    }
                    if(possiblePositions < 2)
                    {
                        if(y - 3 >= 0)
                        {
                            if(statusArray[x][y - 3] == cellStatus.hit && statusArray[x][y - 2] == cellStatus.hit && statusArray[x][y - 1] == cellStatus.hit)
                            {
                                up = true; // could be up
                                possiblePositions++;
                            }

                            if(possiblePositions < 2)
                            {
                                if(y + 3 <= 9)
                                {
                                    if(statusArray[x][y + 3] == cellStatus.hit && statusArray[x][y + 2] == cellStatus.hit && statusArray[x][y + 1] == cellStatus.hit)
                                    {
                                        down = true; // could be down
                                        possiblePositions++;
                                    }
                                }
                            }
                            if(possiblePositions < 2)
                            {
                                if(statusArray[x][y-1] == cellStatus.hit && statusArray[x][y+1] == cellStatus.hit && statusArray[x][y+2] == cellStatus.hit)
                                {
                                    middleDown = true; // could be in the middle down
                                    possiblePositions++;
                                }
                            }
                            if(possiblePositions < 2)
                            {
                                if(statusArray[x][y+1] == cellStatus.hit && statusArray[x][y-1] == cellStatus.hit && statusArray[x][y-2] == cellStatus.hit)
                                {
                                    middleUp = true; // could be in the middle up
                                    possiblePositions++;
                                }
                            }
                        }
                    }
                            
                    if(possiblePositions == 1) // update status to sunk
                    {
                        numHits-=3;
                        numSunk+=3;
                        ghostBattleship.set(-1, -1);
                        if(left == true)
                        {
                            statusArray[x - 1][y] = cellStatus.sunk;
                            statusArray[x - 2][y] = cellStatus.sunk;
                            statusArray[x - 3][y] = cellStatus.sunk;
                        }
                        else if(right == true)
                        {
                            statusArray[x + 1][y] = cellStatus.sunk;
                            statusArray[x + 2][y] = cellStatus.sunk;
                            statusArray[x + 3][y] = cellStatus.sunk;
                        }
                        else if(middleLeft == true)
                        {
                            statusArray[x + 1][y] = cellStatus.sunk;
                            statusArray[x - 1][y] = cellStatus.sunk;
                            statusArray[x - 2][y] = cellStatus.sunk;
                        }
                        else if(middleRight == true)
                        {
                            statusArray[x - 1][y] = cellStatus.sunk;
                            statusArray[x + 1][y] = cellStatus.sunk;
                            statusArray[x + 2][y] = cellStatus.sunk;
                        }
                        else if(up == true)
                        {
                            statusArray[x][y - 1] = cellStatus.sunk;
                            statusArray[x][y - 2] = cellStatus.sunk;
                            statusArray[x][y - 3] = cellStatus.sunk;
                        }
                        else if(down == true)
                        {
                            statusArray[x][y + 1] = cellStatus.sunk;
                            statusArray[x][y + 2] = cellStatus.sunk;
                            statusArray[x][y + 3] = cellStatus.sunk;
                        }
                        else if(middleDown == true)
                        {
                            statusArray[x][y - 1] = cellStatus.sunk;
                            statusArray[x][y + 1] = cellStatus.sunk;
                            statusArray[x][y + 2] = cellStatus.sunk;
                        }
                        else if(middleUp == true)
                        {
                            statusArray[x][y + 1] = cellStatus.sunk;
                            statusArray[x][y - 1] = cellStatus.sunk;
                            statusArray[x][y - 2] = cellStatus.sunk;
                        }
                    }
        
                }
                break;
            }
            case carrier:
            {
                sunkCarrier = true;
                if(numHits == 5)
                {
                    numHits = 0;
                    numSunk += 4;
                    ghostCarrier.set(-1, -1);
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
                    if(x - 3 >= 0)
                    {
                        if(statusArray[x - 3][y] == cellStatus.hit && statusArray[x - 2][y] == cellStatus.hit && statusArray[x - 1][y] == cellStatus.hit)
                        {
                            left = true; // could be to the left
                            possiblePositions++;
                        }
                    }
                    if(x + 3 <= 9)
                    {
                        if(statusArray[x + 3][y] == cellStatus.hit && statusArray[x + 2][y] == cellStatus.hit && statusArray[x + 1][y] == cellStatus.hit)
                        {
                        right = true; // could be to the right
                            possiblePositions++;
                        }
                    }
                    if(statusArray[x + 1][y] == cellStatus.hit && statusArray[x - 1][y] == cellStatus.hit && statusArray[x - 2][y] == cellStatus.hit)
                    {
                        middleLeft = true; // could be in the middle left
                        possiblePositions++;
                    }
                    if(statusArray[x - 1][y] == cellStatus.hit && statusArray[x + 1][y] == cellStatus.hit && statusArray[x + 2][y] == cellStatus.hit)
                    {
                        middleRight = true; // could be in the middle right
                        possiblePositions++;
                    }
                    if(possiblePositions < 2)
                    {
                        if(y - 3 >= 0)
                        {
                            if(statusArray[x][y - 3] == cellStatus.hit && statusArray[x][y - 2] == cellStatus.hit && statusArray[x][y - 1] == cellStatus.hit)
                            {
                                up = true; // could be up
                                possiblePositions++;
                            }

                            if(possiblePositions < 2)
                            {
                                if(y + 3 <= 9)
                                {
                                    if(statusArray[x][y + 3] == cellStatus.hit && statusArray[x][y + 2] == cellStatus.hit && statusArray[x][y + 1] == cellStatus.hit)
                                    {
                                        down = true; // could be down
                                        possiblePositions++;
                                    }
                                }
                            }
                            if(possiblePositions < 2)
                            {
                                if(statusArray[x][y-1] == cellStatus.hit && statusArray[x][y+1] == cellStatus.hit && statusArray[x][y+2] == cellStatus.hit)
                                {
                                    middleDown = true; // could be in the middle down
                                    possiblePositions++;
                                }
                            }
                            if(possiblePositions < 2)
                            {
                                if(statusArray[x][y+1] == cellStatus.hit && statusArray[x][y-1] == cellStatus.hit && statusArray[x][y-2] == cellStatus.hit)
                                {
                                    middleUp = true; // could be in the middle up
                                    possiblePositions++;
                                }
                            }
                        }
                    }
                            
                    if(possiblePositions == 1) // update status to sunk
                    {
                        numHits-=3;
                        numSunk+=3;
                        ghostCarrier.set(-1, -1);
                        if(left == true)
                        {
                            statusArray[x - 1][y] = cellStatus.sunk;
                            statusArray[x - 2][y] = cellStatus.sunk;
                            statusArray[x - 3][y] = cellStatus.sunk;
                        }
                        else if(right == true)
                        {
                            statusArray[x + 1][y] = cellStatus.sunk;
                            statusArray[x + 2][y] = cellStatus.sunk;
                            statusArray[x + 3][y] = cellStatus.sunk;
                        }
                        else if(middleLeft == true)
                        {
                            statusArray[x + 1][y] = cellStatus.sunk;
                            statusArray[x - 1][y] = cellStatus.sunk;
                            statusArray[x - 2][y] = cellStatus.sunk;
                        }
                        else if(middleRight == true)
                        {
                            statusArray[x - 1][y] = cellStatus.sunk;
                            statusArray[x + 1][y] = cellStatus.sunk;
                            statusArray[x + 2][y] = cellStatus.sunk;
                        }
                        else if(up == true)
                        {
                            statusArray[x][y - 1] = cellStatus.sunk;
                            statusArray[x][y - 2] = cellStatus.sunk;
                            statusArray[x][y - 3] = cellStatus.sunk;
                        }
                        else if(down == true)
                        {
                            statusArray[x][y + 1] = cellStatus.sunk;
                            statusArray[x][y + 2] = cellStatus.sunk;
                            statusArray[x][y + 3] = cellStatus.sunk;
                        }
                        else if(middleDown == true)
                        {
                            statusArray[x][y - 1] = cellStatus.sunk;
                            statusArray[x][y + 1] = cellStatus.sunk;
                            statusArray[x][y + 2] = cellStatus.sunk;
                        }
                        else if(middleUp == true)
                        {
                            statusArray[x][y + 1] = cellStatus.sunk;
                            statusArray[x][y - 1] = cellStatus.sunk;
                            statusArray[x][y - 2] = cellStatus.sunk;
                        }
                    }
        
                }
                break;
            }
            default:
            {
                break;
            }
        }     
    }





}
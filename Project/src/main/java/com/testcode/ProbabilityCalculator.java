package Project.src.main.java.com.testcode;

// Probability Calculator Algorithm
// Marcus King



public class ProbabilityCalculator
{

    // keep track of which boats are sunk
    private Boolean sunkDestroyer = false, sunkCruiser = false, sunkSub = false, 
    sunkBattleship = false, sunkCarrier = false;

    private int numShipsSunk = 0;

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

    // keep track of number of guesses for probability calculations later
    private int numHits = 0, numSunk = 0, numMisses = 0;
    private int totalProbabilityScore = 0;

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
        // initialize probability array
        addProbabilities(2);
        addProbabilities(3);
        addProbabilities(3);
        addProbabilities(4);
        addProbabilities(5);
        calculate();
    }

    public double[][] getProbabilityArray()
    {
        return cellProbabilityArray;
    }

    // takes status of cell just guessed (should be hit or miss), x/y coordinates of guess,
    // and type of ship sunk (none, destroyer, cruiser, sub, battleship, carrier)
    public int calculate(Boolean hit, int x, int y, shipSunk typeSunk)
    {

        // Update game state from new info
        if(hit == false) // guess was a miss
        {
            numMisses++;
            statusArray[y][x] = cellStatus.miss;
        }
        else // guess was a hit
        {
            
            if(typeSunk == shipSunk.none) // a ship was not sunk
            {
                numHits++;
                statusArray[y][x] = cellStatus.hit; // update cellStatus to hit
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
                sunkDestroyer = true;
                updateSunk(ghostDestroyer.x, ghostDestroyer.y, shipSunk.destroyer); // call function to convert hits to sunk
            }
            if(ghostCruiser.x > -1 && ghostCruiser.y > -1)
            {
                sunkCruiser = true;
                updateSunk(ghostCruiser.x, ghostCruiser.y, shipSunk.cruiser); // call function to convert hits to sunk
            }
            if(ghostSub.x > -1 && ghostSub.y > -1)
            {
                sunkSub = true;
                updateSunk(ghostSub.x, ghostSub.y, shipSunk.sub); // call function to convert hits to sunk
            }
            if(ghostBattleship.x > -1 && ghostBattleship.y > -1)
            {
                sunkBattleship = true;
                updateSunk(ghostBattleship.x, ghostBattleship.y, shipSunk.battleship); // call function to convert hits to sunk
            }
            if(ghostCarrier.x > -1 && ghostCarrier.y > -1)
            {
                sunkCarrier = true;
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

        

        int size = 0;
        if(sunkDestroyer == false)
        {
            size = 2;
            addProbabilities(size);
        }
        if(sunkCruiser == false)
        {
            size = 3;
            addProbabilities(size);
        }
        if(sunkSub == false)
        {
            size = 3;
            addProbabilities(size);
        }
        if(sunkBattleship == false)
        {
            size = 4;
            addProbabilities(size);
        }
        if(sunkCarrier == false)
        {
            size = 5;
            addProbabilities(size);        
        }
        calculate();
        return 1;
    }

    // add up number of possible hits for each cell for the given ship size and update probability array
    private void addProbabilities(int size)
    {

        // reset probability array
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                cellProbabilityArray[i][j] = 0;
            }
        }

        totalProbabilityScore = 0;

        Boolean lastShip = false;
        if(numShipsSunk == 4 && numHits > 0)
        {
          lastShip = true;
        }

        for (int y = 0; y < 10; y++) // rows
        {
            for(int x = 0; x < 10; x++) // columns
            {
                if(statusArray[y][x] == cellStatus.blank || statusArray[y][x] == cellStatus.hit)
                {
                    
                    if((x + (size-1)) <= 9) // check horizontal for misses or sunk
                    {
                        int hitFactor = 0;
                        boolean validPosition = true;
                        for(int i = 0; i < size; i++)
                        {
                            if(statusArray[y][x + i] == cellStatus.sunk || statusArray[y][x + i] == cellStatus.miss)
                            {
                                validPosition = false;
                            }
                            else if(statusArray[y][x + i] == cellStatus.hit)
                            {
                                hitFactor++;
                            }

                            if(lastShip == true && hitFactor != numHits)
                            {
                                validPosition = false;
                            }
                        }
                        if(validPosition = true) // update probabilities of the cells
                        {
                            for(int i = 0; i < size; i++)
                            {
                                if(statusArray[y][x + i] == cellStatus.blank)
                                {
                                    cellProbabilityArray[y][x + i] += 1 + hitFactor;
                                    totalProbabilityScore += 1 + hitFactor;
                                }
                            }
                        }
                    }

                    if((y + (size-1)) <= 9) // check vertical for misses or sunk
                    {
                        int hitFactor = 0;
                        boolean validPosition = true;
                        for(int i = 0; i < size; i++)
                        {
                            if(statusArray[y + i][x] == cellStatus.sunk || statusArray[y + i][x] == cellStatus.miss)
                            {
                                validPosition = false;
                            }
                            else if(statusArray[y + i][x] == cellStatus.hit)
                            {
                                hitFactor++;
                            }

                            if(lastShip == true && hitFactor != numHits)
                            {
                                validPosition = false;
                            }
                        }
                        if(validPosition = true) // update probabilities of the cells
                        {
                            for(int i = 0; i < size; i++)
                            {
                                if(statusArray[y + i][x] == cellStatus.blank)
                                {
                                    cellProbabilityArray[y + i][x] += 1 + hitFactor;
                                    totalProbabilityScore += 1 + hitFactor;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // calculate final probability for each cell
    private void calculate()
    {
        int hp = 17 - (numHits + numSunk);
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                cellProbabilityArray[i][j] = (cellProbabilityArray[i][j] / totalProbabilityScore) * hp;
            }
        }
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
                        if(statusArray[y][x - 1] == cellStatus.hit)
                        {
                            left = true; // could be to the left
                            possiblePositions++;
                        }
                    }
                    if(x + 1 <= 9)
                    {
                        if(statusArray[y][x + 1] == cellStatus.hit)
                        {
                        right = true; // could be to the right
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y - 1 >= 0)
                    {                    
                        if(statusArray[y - 1][x] == cellStatus.hit)
                        {
                            up = true; // could be up
                            possiblePositions++;
                        }

                        if(possiblePositions < 2 && y + 1 >= 0)
                        {
                            if(y + 1 <= 9)
                            {
                                if(statusArray[y + 1][x] == cellStatus.hit)
                                {
                                   down = true; // could be down
                                   possiblePositions++;
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
                            statusArray[y][x - 1] = cellStatus.sunk;
                        }
                        else if(right == true)
                        {
                            statusArray[y][x + 1] = cellStatus.sunk;
                        }
                        else if(up == true)
                        {
                            statusArray[y - 1][x] = cellStatus.sunk;
                        }
                        else if(down == true)
                        {
                            statusArray[y + 1][x] = cellStatus.sunk;
                        }
                    }

                }
                break;
            }
            case sub: // sub and cruiser are same length so calculations are the same
            {
            }
            case cruiser:
            {
                
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
                else // check positions around hit for possible sunk ship. //If only one possible position, update status of those cells to sunk
                {
                    if(x - 2 >= 0)
                    {
                        if(statusArray[y][x - 2] == cellStatus.hit && statusArray[y][x - 1] == cellStatus.hit)
                        {
                            left = true; // could be to the left
                            possiblePositions++;
                        }
                    }
                    if(x + 2 <= 9)
                    {
                        if(statusArray[y][x + 2] == cellStatus.hit && statusArray[y][x + 1] == cellStatus.hit)
                        {
                        right = true; // could be to the right
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && x+1 <= 9 && x - 1 >= 0)
                    {
                        if(statusArray[y][x + 1] == cellStatus.hit && statusArray[y][x - 1] == cellStatus.hit)
                        {
                            middleHorizontal = true; // could be in the middle
                            possiblePositions++;
                        }
                    }
                    
                    if(possiblePositions < 2 && y - 2 >= 0)
                    {
                        if(statusArray[y - 2][x] == cellStatus.hit && statusArray[y - 1][x] == cellStatus.hit)
                        {
                            up = true; // could be up
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y + 2 <= 9)
                    {
                                
                        if(statusArray[y + 2][x] == cellStatus.hit && statusArray[y + 1][x] == cellStatus.hit)
                        {
                            down = true; // could be down
                            possiblePositions++;
                        }      
                    }
                    if(possiblePositions < 2 && y + 1 <= 9 && y - 1 >= 0)
                    {
                        if(statusArray[y+1][x] == cellStatus.hit && statusArray[y-1][x] == cellStatus.hit)
                        {
                            middleVerticle = true; // could be in the middle
                            possiblePositions++;
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
                            statusArray[y][x - 1] = cellStatus.sunk;
                            statusArray[y][x - 2] = cellStatus.sunk;
                        }
                        else if(right == true)
                        {
                            statusArray[y][x + 1] = cellStatus.sunk;
                            statusArray[y][x + 2] = cellStatus.sunk;
                        }
                        else if(middleHorizontal == true)
                        {
                            statusArray[y][x + 1] = cellStatus.sunk;
                            statusArray[y][x - 1] = cellStatus.sunk;
                        }
                        else if(up == true)
                        {
                            statusArray[y - 1][x] = cellStatus.sunk;
                            statusArray[y - 2][x] = cellStatus.sunk;
                        }
                        else if(down == true)
                        {
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y + 2][x] = cellStatus.sunk;
                        }
                        else if(middleVerticle == true)
                        {
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y - 1][x] = cellStatus.sunk;
                        }
                    }
                }
                break;
            }
            case battleship:
            {
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
                        if(statusArray[y][x - 3] == cellStatus.hit && statusArray[y][x - 2] == cellStatus.hit && statusArray[y][x - 1] == cellStatus.hit)
                        {
                            left = true; // could be to the left
                            possiblePositions++;
                        }
                    }
                    if(x + 3 <= 9)
                    {
                        if(statusArray[y][x + 3] == cellStatus.hit && statusArray[y][x + 2] == cellStatus.hit && statusArray[y][x + 1] == cellStatus.hit)
                        {
                        right = true; // could be to the right
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && x + 1 <= 9 && x - 2 >= 0)
                    {
                        if(statusArray[y][x + 1] == cellStatus.hit && statusArray[y][x - 1] == cellStatus.hit && statusArray[y][x - 2] == cellStatus.hit)
                        {
                            middleLeft = true; // could be in the middle left
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && x + 2 <= 9 && x - 1 >= 0)
                    {
                        if(statusArray[y][x - 1] == cellStatus.hit && statusArray[y][x + 1] == cellStatus.hit && statusArray[y][x + 2] == cellStatus.hit)
                        {
                            middleRight = true; // could be in the middle right
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y - 3 >= 0)
                    {
                      
                        if(statusArray[y - 3][x] == cellStatus.hit && statusArray[y - 2][x] == cellStatus.hit && statusArray[y - 1][x] == cellStatus.hit)
                        {
                            up = true; // could be up
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y + 3 <= 9)
                    {        
                        if(statusArray[y + 3][x] == cellStatus.hit && statusArray[y + 2][x] == cellStatus.hit && statusArray[y + 1][x] == cellStatus.hit)
                        {
                            down = true; // could be down
                            possiblePositions++;
                        }         
                    }
                    if(possiblePositions < 2 && y - 1 >= 0 && y + 2 <= 9)
                    {
                        if(statusArray[y-1][x] == cellStatus.hit && statusArray[y+1][x] == cellStatus.hit && statusArray[y+2][x] == cellStatus.hit)
                        {
                            middleDown = true; // could be in the middle down
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y - 2 >= 0 && y + 1 <= 9)
                    {
                        if(statusArray[y+1][x] == cellStatus.hit && statusArray[y-1][x] == cellStatus.hit && statusArray[y-2][x] == cellStatus.hit)
                        {
                            middleUp = true; // could be in the middle up
                            possiblePositions++;
                        }
                    }
                                
                    if(possiblePositions == 1) // update status to sunk
                    {
                        numHits-=3;
                        numSunk+=3;
                        ghostBattleship.set(-1, -1);
                        if(left == true)
                        {
                            statusArray[y][x - 1] = cellStatus.sunk;
                            statusArray[y][x - 2] = cellStatus.sunk;
                            statusArray[y][x - 3] = cellStatus.sunk;
                        }
                        else if(right == true)
                        {
                            statusArray[y][x + 1] = cellStatus.sunk;
                            statusArray[y][x + 2] = cellStatus.sunk;
                            statusArray[y][x + 3] = cellStatus.sunk;
                        }
                        else if(middleLeft == true)
                        {
                            statusArray[y][x + 1] = cellStatus.sunk;
                            statusArray[y][x - 1] = cellStatus.sunk;
                            statusArray[y][x - 2] = cellStatus.sunk;
                        }
                        else if(middleRight == true)
                        {
                            statusArray[y][x - 1] = cellStatus.sunk;
                            statusArray[y][x + 1] = cellStatus.sunk;
                            statusArray[y][x + 2] = cellStatus.sunk;
                        }
                        else if(up == true)
                        {
                            statusArray[y - 1][x] = cellStatus.sunk;
                            statusArray[y - 2][x] = cellStatus.sunk;
                            statusArray[y - 3][x] = cellStatus.sunk;
                        }
                        else if(down == true)
                        {
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y + 2][x] = cellStatus.sunk;
                            statusArray[y + 3][x] = cellStatus.sunk;
                        }
                        else if(middleDown == true)
                        {
                            statusArray[y - 1][x] = cellStatus.sunk;
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y + 2][x] = cellStatus.sunk;
                        }
                        else if(middleUp == true)
                        {
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y - 1][x] = cellStatus.sunk;
                            statusArray[y - 2][x] = cellStatus.sunk;
                        }
                    }
        
                }
                break;
            }
            case carrier:
            {
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
                    if(x - 4 >= 0)
                    {
                        if(statusArray[y][x - 4] == cellStatus.hit && statusArray[y][x - 3] == cellStatus.hit && statusArray[y][x - 2] == cellStatus.hit && statusArray[y][x - 1] == cellStatus.hit)
                        {
                            left = true; // could be to the left
                            possiblePositions++;
                        }
                    }
                    if(x + 4 <= 9)
                    {
                        if(statusArray[y][x + 4] == cellStatus.hit && statusArray[y][x + 3] == cellStatus.hit && statusArray[y][x + 2] == cellStatus.hit && statusArray[y][x + 1] == cellStatus.hit)
                        {
                            right = true; // could be to the right
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && x + 1 <= 9 && x - 3 >= 0)
                    {
                        if(statusArray[y][x + 1] == cellStatus.hit && statusArray[y][x - 1] == cellStatus.hit && statusArray[y][x - 2] == cellStatus.hit && statusArray[y][x - 3] == cellStatus.hit)
                        {
                            middleLeft = true; // could be in the middle left
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && x + 3 <= 9 && x - 1 >= 0)
                    {
                        if(statusArray[y][x - 1] == cellStatus.hit && statusArray[y][x + 1] == cellStatus.hit && statusArray[y][x + 2] == cellStatus.hit && statusArray[y][x + 3] == cellStatus.hit)
                        {
                            middleRight = true; // could be in the middle right
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && x + 2 <= 9 && x - 2 >= 0)
                    {
                        if(statusArray[y][x - 2] == cellStatus.hit && statusArray[y][x - 1] == cellStatus.hit && statusArray[y][x + 1] == cellStatus.hit && statusArray[y][x + 2] == cellStatus.hit)
                        {
                            middleHorizontal = true; // could be in the middle
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y - 4 >= 0)
                    {
                        if(statusArray[y - 4][x] == cellStatus.hit && statusArray[y - 3][x] == cellStatus.hit && statusArray[y - 2][x] == cellStatus.hit && statusArray[y - 1][x] == cellStatus.hit)
                        {
                            up = true; // could be up
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y + 3 <= 9)
                    {  
                        if(statusArray[y + 4][x] == cellStatus.hit && statusArray[y + 3][x] == cellStatus.hit && statusArray[y + 2][x] == cellStatus.hit && statusArray[y + 1][x] == cellStatus.hit)
                        {
                            down = true; // could be down
                            possiblePositions++;
                        } 
                    }
                    if(possiblePositions < 2 && y + 3 <= 9 && y - 1 >= 0)
                    {
                        if(statusArray[y-1][x] == cellStatus.hit && statusArray[y+1][x] == cellStatus.hit && statusArray[y+2][x] == cellStatus.hit && statusArray[y+3][x] == cellStatus.hit)
                        {
                            middleDown = true; // could be in the middle down
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y + 1 <= 9 && y - 3 >= 0)
                    {
                        if(statusArray[y+1][x] == cellStatus.hit && statusArray[y-1][x] == cellStatus.hit && statusArray[y-2][x] == cellStatus.hit && statusArray[y-3][x] == cellStatus.hit)
                        {
                            middleUp = true; // could be in the middle up
                            possiblePositions++;
                        }
                    }
                    if(possiblePositions < 2 && y + 2 <= 9 && y - 2 >= 0)
                    {
                        if(statusArray[y+2][x] == cellStatus.hit && statusArray[y+1][x] == cellStatus.hit && statusArray[y-1][x] == cellStatus.hit && statusArray[y-2][x] == cellStatus.hit)
                        {
                            middleUp = true; // could be in the middle vertical
                            possiblePositions++;
                        }
                    }
                                 
                    if(possiblePositions == 1) // update status to sunk
                    {
                        numHits-=4;
                        numSunk+=4;
                        ghostCarrier.set(-1, -1);
                        if(left == true)
                        {
                            statusArray[y][x - 1] = cellStatus.sunk;
                            statusArray[y][x - 2] = cellStatus.sunk;
                            statusArray[y][x - 3] = cellStatus.sunk;
                            statusArray[y][x - 4] = cellStatus.sunk;
                        }
                        else if(right == true)
                        {
                            statusArray[y][x + 1] = cellStatus.sunk;
                            statusArray[y][x + 2] = cellStatus.sunk;
                            statusArray[y][x + 3] = cellStatus.sunk;
                            statusArray[y][x + 4] = cellStatus.sunk;
                        }
                        else if(middleLeft == true)
                        {
                            statusArray[y][x + 1] = cellStatus.sunk;
                            statusArray[y][x - 1] = cellStatus.sunk;
                            statusArray[y][x - 2] = cellStatus.sunk;
                            statusArray[y][x - 3] = cellStatus.sunk;

                        }
                        else if(middleRight == true)
                        {
                            statusArray[y][x - 1] = cellStatus.sunk;
                            statusArray[y][x + 1] = cellStatus.sunk;
                            statusArray[y][x + 2] = cellStatus.sunk;
                            statusArray[y][x + 3] = cellStatus.sunk;
                        }
                        else if(up == true)
                        {
                            statusArray[y - 1][x] = cellStatus.sunk;
                            statusArray[y - 2][x] = cellStatus.sunk;
                            statusArray[y - 3][x] = cellStatus.sunk;
                            statusArray[y - 4][x] = cellStatus.sunk;
                        }
                        else if(down == true)
                        {
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y + 2][x] = cellStatus.sunk;
                            statusArray[y + 3][x] = cellStatus.sunk;
                            statusArray[y + 4][x] = cellStatus.sunk;
                        }
                        else if(middleDown == true)
                        {
                            statusArray[y - 1][x] = cellStatus.sunk;
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y + 2][x] = cellStatus.sunk;
                            statusArray[y + 3][x] = cellStatus.sunk;
                        }
                        else if(middleUp == true)
                        {
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y - 1][x] = cellStatus.sunk;
                            statusArray[y - 2][x] = cellStatus.sunk;
                            statusArray[y - 3][x] = cellStatus.sunk;
                        }
                        else if(middleVerticle == true)
                        {
                            statusArray[y + 2][x] = cellStatus.sunk;
                            statusArray[y + 1][x] = cellStatus.sunk;
                            statusArray[y - 2][x] = cellStatus.sunk;
                            statusArray[y - 1][x] = cellStatus.sunk;
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
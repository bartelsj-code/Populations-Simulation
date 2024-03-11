import java.util.*;

/* The Animal class is the superclass for Bobbles and Friggles.
It consists mainly of getters and setters with some shared more complex methods.*/
public class Animal{
////////////////////////Variables//////////////////////
  //tracks whether animal is alive
  private boolean alive;
  //keeps track of how much energy an animal has.
  protected int energyLevel;
  //energyLevel required to 
  private int reproductionEnergyThreshold;
  //keeps track of how many timeSteps this animal has survived
  private int age = 0;
  //stores age at which animal should die of old age
  private int maxAge;
  //a pointer to a landplot object that indicates a animals location
  private LandPlot currentLandPlot;
  //the x coordinate of an animal's location
  private int xCoord;
  //the y coordinate of an animal's location
  private int yCoord;
  //what type of animal this is (very useful for debugging but not strictly neccessary)
  private String type;
  //the grid of LandPlot objects the animal lives in.
  private LandPlot[][] environmentGrid;
  //the height of the grid
  private int environmentHeight;
  //the width of the grid
  private int environmentWidth;
  //the maximum distance an animal can jump between landplots
  private int moveDistance;
  //how often an animal jumps between LandPlot objects on its turn
  protected int moveCount;
  //how large the litter will be when an animal reproduces (offspring count)
  protected int babyCount;

//////////////////////Vitals////////////////////////

  /**
   * sets Alive boolean variable 
   * @param stat boolean alive/dead status of animal
   */
  public void setAlive(boolean stat){
    alive = stat;
  }

  /**
   *Return true if alive, false if not alive
   * @return boolean of alive status
   */
  public boolean isAlive(){
    return alive;
  }

//////////////////////Mapping///////////////////////

  /**
   *Sets an animal's environmentGrid to its environment and its dimensions environmentHeight and environmentWidth. 
   * @param eg LandPlot[][] is the grid of landplots that constitutes the environment
   */
  public void setEnvironmentGrid(LandPlot[][] eg){
    environmentGrid = eg;
    environmentHeight = environmentGrid[0].length;
    environmentWidth = environmentGrid.length;
  }
  
  /**
   * Returns the width of the grid the animal lives in
   * @return int environmentWidth which is the width of the grid
   */
  public int getEnvironmentWidth(){
    return environmentWidth;
  }

  /**
   * Returns the height of the grid the animal lives in
   * @return int environmentHeight which is the width of the grid
   */
  public int getEnvironmentHeight(){
    return environmentHeight;
  }
  
  /**
   * Returns the environment the animal lives in.
   * @return LandPlot[][] environmentGrid which is the grid the animal lives in
   */
  public LandPlot[][] getEnvironmentGrid(){
    return environmentGrid;
  }
////////////////////////Age//////////////////////////

  /**
   * Set maxAge of animal
   * @param ma int is the maximum age a animal can live to
   */
  public void setMaxAge(int ma){
    maxAge = ma;
  }
  
  /**
   * Returns maxAge for animal
   * @return maxAge int is the maximum age a animal can live to
   */
  public int getMaxAge(){
    return maxAge;
  }
  
  /**
   *Returns the age of the animal
   * @return age int the current age of the animal
   */
  public int getAge(){
    return age;
  }

  /**
   *increases age of animal by one.
   */
  public void age(){
    age+=1;
  }

////////////////////////Energy//////////////////////////
  /**
   *Returns the energy level of an animal
   * @return energyLevel int - the energy level an animal is at
   */
  public int getEnergyLevel(){
    return energyLevel;
  }
  
  /**
   *Sets the energy level of an animal to a certain value
   * @param en int - energy level of an animal
   */
  public void setEnergyLevel(int en){
    energyLevel = en;
  }
  
  /**
   * Adds positive or negative value to energy level as an incremental adjustment
   * @param en int - level by which energy should be incremented
   */
  public void incrementEnergy(int en){
    energyLevel += en;
  }
  /**
   *Returns an integer which will then be used as the new energy level for self and offspring
   *@param denominator int - how many ways should the energy level be split
   *@return energyLevel/denominator int - current energyLevel devided by denominator
   */
  public int splitEnergy(int denominator){
    return energyLevel/denominator;
  }

////////////////////////Type///////////////////////////////
  /**
   *Sets own type variable to its own type
   *@param type1 String - the type of animal that you are
   */
  public void setType(String type1){
    type = type1;
  }

///////////////////////Location//////////////////////////
  /**
   *sets xCoord - x coordinate of animal in grid
   *@param newX int - x coordinate in grid
   */
  public void setX(int newX){
    xCoord = newX;
  }

  /**
   *sets yCoord - y coordinate of animal in grid
   *@param newY int - y coordinate in grid
   */
  public void setY(int newY){
    yCoord = newY;
  }

  /**
   * Returns the x coordinate of an animal
   * @return xCoord int - x coordinate of animal
   */
  public int getX(){
    return xCoord;
  }
  
  /**
   * Returns the y coordinate of an animal
   * @return yCoord int - y coordinate of animal
   */
  public int getY(){
    return yCoord;
  }
  
  /**
   * Returns the land plot on which the animal is currently living
   * @return currentLandPlot LandPlot - current residence
   */
  public LandPlot getLandPlot(){
    return currentLandPlot;
  }
  
  /**
   *sets currentLandPlot to a landplot and adjusts xCoord and yCoord accordingly
   *@param lp LandPlot - landplot that animal is being placed on
   */
  public void setLandPlot(LandPlot lp){
    currentLandPlot = lp;
    if(lp!=null){
      xCoord = lp.getX();
      yCoord = lp.getY();
    }
  }

////////////////////////Movement////////////////////
  /**
   *Returns a Landplot from the grid within moveDistance of the current landplot.
   *@return nlp LandPlot - the new landPlot that the animal will move to.
   */
  public LandPlot findNewHome(){
    //retrieve distances to the four sides
    int distanceToTop = (environmentHeight-1) - yCoord;
    int distanceToBottom = yCoord;
    int distanceToLeft = xCoord;
    int distanceToRight = (environmentWidth-1) - xCoord;
    //get an four int values for movement in the four compass directions while staying within grid
    int upMotion = determineMovement(distanceToTop);
    int downMotion = determineMovement(distanceToBottom);
    int rightMotion = determineMovement(distanceToRight);
    int leftMotion = determineMovement(distanceToLeft);
    //adds north/south motion
    int verticalMotion = upMotion-downMotion;
    //adds east/west motion
    int horizontalMotion = rightMotion-leftMotion;
    //returns a landplot at the new location
    LandPlot nlp = environmentGrid[xCoord+horizontalMotion][yCoord+verticalMotion];
    return nlp;
  }

  /**
   *Sets moveCount - the amount of moves an animal does during one timeStep
   *@param mc int - moves per timestep
   */
  public void setMoveCount(int mc){
    moveCount = mc;
  }
  
  /**
   *Returns moveCount - the amount of moves an animal does during one timeStep
   *@return int moveCount - moves per timestep
   */
  public int getMoveCount(){
    return moveCount;
  }

  /**
   *Returns: Calculates how far an animal will move in a certain direction based on how large its moveDistance and how far it is from an age
   *@param distanceToEdge int - distance from the edge of grid in a certain direction
   *@return actualMovment int - how far the animal will actually move in a direction
   */
  public int determineMovement(int distanceToEdge){
    int range;
    if(distanceToEdge <= moveDistance){
      range = distanceToEdge;
    }else{
      range = moveDistance;
    }
    int actualMovment = (int)Math.floor(Math.random()*(range+1));
    return actualMovment;

  }

  /**
   *Sets moveDistance - maximum range an animal can move in a certain direction
   *@param md int - maximum distance an animal can move in the x or y direction
   */
  public void setMoveDistance(int md){
    moveDistance = md;
  }

  /**
   *Returns the distance an animal can maximally move
   *@return moveDistance int - range
   */
  public int getMoveDistance(){
    return moveDistance;
  }

///////////////////////Reproduction////////////////////////////
  /**
   *Returns the energylevel an animal must have to reproduce
   *@return reproductionEnergyThreshold int - reproduction energy threshold
   */
  public int getReproductionEnergyThreshold(){
    return reproductionEnergyThreshold;
  }

  /**
   *sets the threshold energy required for reproduction
   *@param ret int - reproductionEnergyThreshold
   */
  public void setReproductionEnergyThreshold(int ret){
    reproductionEnergyThreshold = ret;
  }
  
  /**
   *Returns the size that an animal's litter will be
   *@return babyCount int - litter size
   */
  public int getBabyCount(){
    return babyCount;
  }
  
  /**
   *sets the litter size
   *@param bc int - baby count
   */
  public void setBabyCount(int bc){
    babyCount = bc;
  }
//////////////////////////Mutation///////////////////////
 
  /**
   *Returns a possibly altered integer with a certain chance
   *@param attribute int - integer that attribute is at
   *@param mutationChance float - chance of mutation of certain attribute
   *@return attribute int
   */
  public int mutate(int attribute, float mutationChance){
    if(mutationChance != 0){
      if(Math.random()<=mutationChance){
        if((int)Math.floor(Math.random())==0){
          attribute -= 1;
        }else{
          attribute += 1;
        }
      }
    }
    return attribute;
  }
}


import java.util.*;
/**
 *LandPlot objects are what the environment is made of and the place where beeterweed grows
 */
public class LandPlot{
///////////////////////////////Variables///////////////////////////
  //own x coordinate
  private int xCoord;
  //own y coordinate
  private int yCoord;
  //time it takes beeterweed to grow back
  private int regrowthTime;
  //true if beeterweed is growing
  private boolean hasBeeterWeed;
  //counter which keeps track of how many days have passed since beeterweed was eaten
  private int regrowthCounter = regrowthTime;
  //speed of regrowth (should stay 1)
  private int regrowthPerDay = 1;
  //PQ of friggles located at this landplot, a min PQ by energy level
  private FrigglePQ friggleList;
  //list of the bobbles located at this landplot
  private ArrayList<Bobble> bobbleList;

//////////////////////////////Constructor//////////////////////////

  /**
   *Constructor, Sets a bunch of stuff ( see below)
   *@param xPosition int - x coordinate of land plot 
   *@param yPosition int - y coordinate of land plot
   *@param beeterWeed boolean - beeterweed status 
   *@param rt int - regrowthTime for beeterweed
   */
  public LandPlot(int xPosition, int yPosition, boolean beeterWeed,int rT){
    xCoord = xPosition;
    yCoord = yPosition;
    hasBeeterWeed = beeterWeed;
    friggleList = new FrigglePQ();
    bobbleList = new ArrayList<Bobble>();
    regrowthTime = rT;
  }
////////////////////////////////getters////////////////////////////
  /**
   *Returns regrowthTime, time it takes beeterweed to grow back after being eaten
   *@return regrowthTime int - beeterweed regrowth time
   */
  public int getRegrowthTime(){
    return regrowthTime;
  }
  
  /**
   *Returns x coord
   *@return xCoord int - x coordinate
   */
  public int getX(){
    return xCoord;
  }
  
  /**
   *Returns y coord
   *@return yCoord int - y coordinate
   */
  public int getY(){
    return yCoord;
  }
  
  /**
   *Returns counter number
   *@return regrowthCounter int - how long it has been since the landplot was grazed upon
   */
  public int getCounter(){
    return regrowthCounter;
  }
  
  /**
   *Returns true if beeterweed is present, else returns false
   *@return hasBeeterWeed boolean - bw status
   */
  public boolean getBeeterWeedStatus() {
    return hasBeeterWeed;
  }
  
  /**
   *Returns list of bobbles at this location
   *@return bobbleList ArrayList<Bobble> - list storing bobbles
   */
  public ArrayList<Bobble> getBobbleList(){
    return bobbleList;
  }
  
  /**
   *Returns friggle priority queue
   *@return ArrayList<Friggle> - PQ
   */
  public ArrayList<Friggle> getFrigglelist(){
    return friggleList.getList();
  }
////////////////////////////////setters////////////////////////////
  /**
   *Sets regrowthTime
   *@param r int - regrowthTime
   */
  public void setRegrowthTime(int r){
    regrowthTime = r;
  }
////////////////////Adding and removing from lists/////////////////
  /**
   *adds a bobble to bobbleList
   *@param mary Bobble
   */
  public void addBobble(Bobble mary){
    bobbleList.add(mary);
  }
  
  /**
   *Removes bobble from bobbleList
   *@param jackson Bobble
   */
  public void removeBobble(Bobble jackson){
    bobbleList.remove(jackson);
  }

  /**
   *adds Friggle to frigglelist (PQ)
   *@param steve Friggle
   */
  public void addFriggle(Friggle steve){
    friggleList.add(steve);
  }
  
  /**
   *removes a friggle from friggleList (PQ) by finding a that friggle in the PQ and removing at that position
   *@param ariana Friggle 
   */
  public void removeFriggle(Friggle ariana){
    int index = 0;
    int found = 0;
    for(Friggle bob: friggleList.getList()){
      if(bob.equals(ariana)){
        found = index;
      }
      index ++;
    }
    friggleList.remove(found);
  }
  
/////////////////// Landplot population, for display///////////////
  /**
   *Returns friggle population here 
   *@return int - amount of friggles at this location
   */
  public int plotFrigglePopulation(){
    return friggleList.getList().size();
  }
  
  /**
   *Returns bobble population here 
   *@return int - amount of bobbles at this location
   */
  public int plotBobblePopulation(){
    return bobbleList.size();
  }
  
//////////////////////////// Bobbles Eating////////////////////////
  /**
   *Returns the weakest/first friggle in friggleList and has that friggle run die() on itself so it is removed from sim
   *@return ben Friggle
   */
  public Friggle huntFriggles(){
    if (friggleList.getList().size() > 0){
      Friggle ben = friggleList.poll();
      ben.die();
    return ben;
    }else{
      return null;
    }
  }

//////////////////////////// Friggles Eating///////////////////////
  /**
   *Returns true if there was beeterweed and  resets beeterweed, else, return false
   *@return boolean
   */
  public boolean beEaten(){
    if (hasBeeterWeed){
      hasBeeterWeed = false;
      regrowthCounter = 0;
      return true;
    }else{
      return false;
    }
  }

/////////////////////////// Daily Actions /////////////////////////
  /**
   *if beeterweed isn't growing there, increase counter. if the counter is high enough, grow beeterweed
   */
  public void age(){
    if(regrowthCounter < regrowthTime){
      regrowthCounter += regrowthPerDay;
    }else{
      hasBeeterWeed = true;
    }
  }
}
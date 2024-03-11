import java.util.*;
/**
 *Friggles, I guess? not sure how to do class comments. this contains everything specific only to friggles.
 */
public class Friggle extends Animal implements Comparable<Friggle>{
/////////////////////////////// Circle of Life /////////////////////////
  /**
   * Method sets a bunch of different animal variables. it is called only once when the animal comes into existance.
   * Non parameter related actions: sets itself to isAlive == true, type == friggle.
   * @param lp LandPlot - landplot where this animal is located
   * @param ma int - maximum age attainable
   * @param el int - energy level to start out with
   * @param eg LandPlot[][] - the environment grid the animal lives on
   * @param md int - maximum distance it can move
   * @param mc int - how often it can move per time step
   * @param bc int - litter size
   * @param ret int - reproduction threshold
   */
  public void materialize(LandPlot lp, int ma, int el, LandPlot[][] eg,int md,int mc,int bc, int ret){
    setFriggleLandPlot(lp);
    setMaxAge(ma);
    setType("friggle");
    setEnergyLevel(el);
    setEnvironmentGrid(eg);
    setMoveDistance(md);
    setAlive(true);
    setBabyCount(bc);
    setReproductionEnergyThreshold(ret);
    setMoveCount(mc);
  }

  /**
   * Performs all actions that make a animal dead.
   * removes itself from the list in the landplot object it is in
   * sets landPlot pointer to null
   * isAlive becomes false
   */
  public void die(){
    LandPlot current = getLandPlot();
    current.removeFriggle(this);
    setLandPlot(null);
    setAlive(false);
  }
  
/////////////////////////////// Daily Actions //////////////////////////
  /**
   *Changes the landplot associated with the animal
   *gets current landPlot (if it exists) and removes itself from that landplot's list
   *redirects landPlot pointer to new LandPlot and adds itself to the list of this new LandPlot
   *@param lp LandPlot - new land plot (the one to move to)
   */
  public void setFriggleLandPlot(LandPlot lp){
    LandPlot old = getLandPlot();
    if(old != null){
      old.removeFriggle(this);
    }
    setLandPlot(lp);
    lp.addFriggle(this);
  }
  
  /**
   *runs the beEaten method of a landplot object, removing its beeterweed. the it increments own energy level by 3
   */
  public void graze(){
    if(getLandPlot().beEaten()){
      incrementEnergy(3);
    } 
  }
  
  /**
   *Does a daily cycle
   *@return offspring ArrayList<Friggle> - the new babies of this individual
   */
  public ArrayList<Friggle> doDailyRoutine(){
    //creates the list of babies in case reproduction happens
    ArrayList<Friggle> offspring = new ArrayList<Friggle>();
    //Check If you are supposed to be dead, if so, die
    if( getAge() >= getMaxAge() || getEnergyLevel() <= 0){
      die();
    }else{
      //Move a certain number of times and graze at each landplot you come across.
      int moves = 0;
      while(moves<getMoveCount()&& getEnergyLevel()>0){//the getEnergyLevel is relevant only if you incrementEnergy energy during each movement.
        moves += 1;
        LandPlot newHome = findNewHome();
        setFriggleLandPlot(newHome);
        //Forrage
        graze();
      }
      //Expend Energy
      incrementEnergy(-1);
      //Baby Time! make/materialize babies and add to the babies list
      if (getEnergyLevel() >= getReproductionEnergyThreshold()){
        int newEnergy=splitEnergy(babyCount+1);
        setEnergyLevel(newEnergy);
        for(int i = 0; i < babyCount; i++){
          Friggle kathy = new Friggle();
          kathy.materialize(getLandPlot(),getMaxAge(),newEnergy,getEnvironmentGrid(),getMoveDistance(),getMoveCount(),getBabyCount(),getReproductionEnergyThreshold());//LandPlot, Maximum Age, Energy Level, areaGrid, Move Distance, babyCount
          offspring.add(kathy);
        }
      }
      
      
      


      //Age 
      age();

  
     
    }
    return offspring;
  }

/////////////////////////////////Priority Queue Stuff///////////////////////////////
  /**
   *Compares two friggles for PQ switching
   *@param b Friggle - a different friggle (that will be in the same landplot)
   *@return own EnergyLevel - the other's EnergyLevel
   */
  public int compareTo(Friggle b){
    return getEnergyLevel() - b.getEnergyLevel();
  }
}
import java.util.*;
/**
 *This is the bobble class. it runs everything bobble related. how they are born,die,and spend their days
 */
public class Bobble extends Animal{

/////////////////////////////// Circle of Life /////////////////////////
  /**
   * Method sets a bunch of different animal variables. it is called only once when the animal comes into existance.
   * Non parameter related actions: sets itself to isAlive == true, type == bobble.
   * @param lp LandPlot - landplot where this animal is located
   * @param ma int - maximum age attainable
   * @param el int - energy level to start out with
   * @param eg LandPlot[][] - the environment grid the animal lives on
   * @param md int - maximum distance it can move
   * @param mc int - how often it can move per time step
   * @param bc int - litter size
   * @param ret int - reproduction threshold
   */
  public void materialize(LandPlot lp, int ma, int el, LandPlot[][] eg,int md,int mc,int bc, int ret, float [] muCh){
	
    setBobbleLandPlot(lp);
    setMaxAge(ma);
    setType("bobble");
    setEnergyLevel(el);
    setEnvironmentGrid(eg);
    setMoveDistance(md);
    setAlive(true);
    setBabyCount(bc);
    setReproductionEnergyThreshold(ret);
    setMoveCount(mc);
	setMutationChances(muCh);
	//System.out.println("level at birth: " + getEnergyLevel());
  }
  
  /**
   * Performs all actions that make a animal dead.
   * removes itself from the list in the landplot object it is in
   * sets landPlot pointer to null
   * isAlive becomes false
   */
  public void die(){
    LandPlot current = getLandPlot();
    current.removeBobble(this);
    setLandPlot(null);
    setAlive(false);
  }

/////////////////////// Daily Actions ///////////////////////
  /**
   *Changes the landplot associated with the animal
   *gets current landPlot (if it exists) and removes itself from that landplot's list
   *redirects landPlot pointer to new LandPlot and adds itself to the list of this new LandPlot
   *@param lp LandPlot - new land plot (the one to move to)
   */
  public void setBobbleLandPlot(LandPlot lp){
    LandPlot old = getLandPlot();
    if(old != null){
      old.removeBobble(this);
    }
    setLandPlot(lp);
    lp.addBobble(this);
  }

  /**
   *checks landplot for friggles. if it finds friggles, eat one of them and increase energy level by that friggles energy
   */
  public void hunt(){
    LandPlot current = getLandPlot();
    Friggle victim = current.huntFriggles();
    if(victim != null){
      incrementEnergy((int)Math.round(victim.getEnergyLevel()*0.5));
    }
  }
  
  /**
   *Performs all the daily actions of a bobble
   *@return offspring ArrayList<Bobble> - newly born bobbles in a list
   */
  public ArrayList<Bobble> doDailyRoutine(){
    ArrayList<Bobble> offspring = new ArrayList<Bobble>();
    //check if you should be dying, if so, die
    if( getAge() >= getMaxAge() || getEnergyLevel() <= 0){
      die();
    }else{
      

      ////////////////reproduce//////////////////
      //check if energy level exceeds threshold
      if (getEnergyLevel() >= getReproductionEnergyThreshold() && getAge() <= 20){
		
        //devide energy level evenly
        int newEnergy=splitEnergy(babyCount+1);
        setEnergyLevel(newEnergy);
        //make and materialize babyCount offspring
		
        for(int i = 0; i < babyCount; i++){
		  
          Bobble milo = new Bobble();
          
          //gets all materialization parameters and mutates as called for
          LandPlot lp = getLandPlot();
          LandPlot[][] eg = getEnvironmentGrid();

		  float[] muCh = getMutationChances();
		  
          int ma = getMaxAge();
          float maMC = muCh[0];
          int el = newEnergy;
          float elMC = muCh[1];
          int md = getMoveDistance();
          float mdMC = muCh[2];
          int mc = getMoveCount();
          float mcMC = muCh[3];
          int bc = getBabyCount();
          float bcMC = muCh[4];
          int rt = getReproductionEnergyThreshold();
          float rtMC = muCh[5];
		
		

          milo.materialize(lp,  mutate(ma,maMC),  mutate(el,elMC),  eg,  mutate(md,mdMC),  mutate(mc,mcMC),  mutate(bc,bcMC),  mutate(rt,rtMC), muCh);
          //LandPlot, Maximum Age, Energy Level, environmentGrid, Move Distance,move Count, babyCount,ReproductionEnergyThreshold
          offspring.add(milo);
        }
		
      }else{
		  //move to a square and hunt. repeat this a MoveCount amount of times
		  int moves = 0;
		  while(moves<getMoveCount() && getEnergyLevel()>0){
			moves += 1;
			
			if(moves  %  4 ==0){
			  setEnergyLevel(getEnergyLevel()-1);
			}
			LandPlot newHome = findNewHome();
			setBobbleLandPlot(newHome); 
			hunt();
		  }
		  //expend energy
		  incrementEnergy(-1);

	  }
      //become older
      age();
    }
    return offspring;
  }
}
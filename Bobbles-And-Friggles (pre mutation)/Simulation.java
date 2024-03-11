import java.util.*;
import java.io.*;
/**
 *Makes the world go round, for bobbles and friggles
 */
public class Simulation {
//////////////////////Variables/////////////////////////
  //width of area
  private int width;
  //height of area
  private int height;
  // grid of Landplots
  private LandPlot[][] areaGrid;
  //all landplots in a one dimensional array, for easy iteration
  private LandPlot[] easyAreaAccess;
  //amount of friggles at start
  private int friggleStartCount;
  //amount of bobbles at start
  private int bobbleStartCount;
  //list in which all living Friggles will be kept
  private ArrayList<Friggle> allFriggleList;
  //list in which all living Bobbles will be kept
  private ArrayList<Bobble> allBobbleList;
  //writer for saving data
  private FileWriter myWriter;
  //data storing list
  private ArrayList<int[]> populations = new ArrayList<int[]>();
  //time that has passed so far
  private int timeStep;
  //type of display
  private int show;
  // bobble attribute list
  private int[] bobbleFacts = new int[6];
  //friggle attribute list
  private int[] friggleFacts = new int[6];
  // landplot attribute list
  private int[] landPlotFacts = new int[1];


///////////////////////Setting up Simulation///////////////////////
  /**
   *Sets sim dimentsions and reads in starting attributes
   *@param a int - width of sim
   *@param b int - height of sim
   */
  public Simulation(int a, int b){
    timeStep = 0;
    width = a;
    height = b;
    getFriggleFacts();
    getBobbleFacts();
    getRGT();
  }
  
  /**
   *creates all the LandPlot objects and places them in areaGrid 2dim array. also adds them to easyAreaAccess 1dim array
   */
  public void fillAreaGrid(){
    areaGrid = new LandPlot[width][height];
    easyAreaAccess = new LandPlot[width*height];
    int tick = 0;
    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        LandPlot newPlot = new LandPlot(x,y,true,landPlotFacts[0]);
        areaGrid[x][y] = newPlot;
        easyAreaAccess[tick] = newPlot;
        tick ++;
      }
    }
  }
  
  /**
   *Creates and materializes all starting bobbles and adds them to allBobbleList
   *@param bsc int - amount of bobbles
   */
  public void addBobbles(int bsc){
    bobbleStartCount = bsc;
    allBobbleList = new ArrayList<Bobble>();
    for(int i = 0; i < bobbleStartCount; i++){
      Bobble charlotte = new Bobble();
      LandPlot charlottesHome = getRandomLandPlot();
      charlotte.materialize(charlottesHome,bobbleFacts[0],bobbleFacts[1],areaGrid,bobbleFacts[2],bobbleFacts[3],bobbleFacts[4],bobbleFacts[5]);
      allBobbleList.add(charlotte);
    }
  }
  
  /**
   *Creates and materializes all starting friggles and adds them to allFriggleList
   *@param fsc int - amount of friggles
   */
  public void addFriggles(int fsc){
    friggleStartCount = fsc;
    allFriggleList = new ArrayList<Friggle>();
    for(int i = 0; i < friggleStartCount; i++){
      Friggle victor = new Friggle();
      LandPlot victorsHome = getRandomLandPlot();
      //LandPlot, Maximum Age, Energy Level, areaGrid, Move Distance,move count,babyCount,threshold,
      victor.materialize(victorsHome,friggleFacts[0],friggleFacts[1],areaGrid,friggleFacts[2],friggleFacts[3],friggleFacts[4],friggleFacts[5]);  
      allFriggleList.add(victor);
    }
  }

  /**
   *Returns a random LandPlot for an animal to start at
   *@return LandPlot - a random landplot
   */
  public LandPlot getRandomLandPlot(){
    int xCoord = (int) Math.floor(Math.random() * width);
    int yCoord = (int) Math.floor(Math.random() * height);
    return areaGrid[xCoord][yCoord];
  }

//////////// Get attributes from Facts.txt files//////////////////
  /**
   *reads a file with a certain name and prints out the lines. 
   *at every line, get the number behind the colon and save that to the type array
   *@param input String - the name of the file from which the data should be extracted
   *@param type int[] - an array of integers with the data specific to a species
   */
  public void getFacts(String input, int[] type){
    System.out.println(input.split("\\.")[0]+":\n");
    String inputFilePath = input;
    File inputFile = new File(inputFilePath);
    Scanner scanner = null;

    try {
      scanner = new Scanner(inputFile);
    } catch (FileNotFoundException e) {
      System.err.println(e);
      System.exit(1);
    }

    int l = 0;
    
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      System.out.println(line);
      type[l] = Integer.parseInt(line.split(":")[1]);
      l++;
    }
    scanner.close();
    System.out.println("\n");
  }

  /**
   *Runs getFacts() for friggles
   */
  public void getFriggleFacts(){
    getFacts("Friggle Facts.txt",friggleFacts);
  }

  /**
   *Runs getFacts() for bobbles
   */
  public void getBobbleFacts(){
    getFacts("Bobble Facts.txt",bobbleFacts);
  }

  /**
   *Runs getFacts() for landplots
   */
  public void getRGT(){
    getFacts("Land Plot Facts.txt",landPlotFacts);
  }

//////////////////////"Graphics"//////////////////
  /**
   *Displays, or doesnt dispay, the visual representation of the environment in the console
   *Really don't feel like breaking this down so this is all the comments I'm going to add for this method
   */
  public void printAreaGrid(){
    if(show != 0){
      String bottom = "#";
      String side = "#";
      if(show == 1){
        bottom = "_";
        side = "|";
      }
      if(show == 2){
        bottom = " ";
        side = " ";
      }
      System.out.println("");
      String displayer;
      for(int i= 0; i<3; i++){
        System.out.print("._");
        for(int x = 0; x < width-1; x++){
          System.out.print("__");
        }
        System.out.print(".  ");
      }
      System.out.println("");
      for(int y = 0; y < height; y++){
        System.out.print("|");
        for(int x = 0; x < width; x++){
          //Beeterweed
          if(areaGrid[x][y].getBeeterWeedStatus()){
            displayer = "M";
          }else{
            displayer = "_";
          }
          System.out.print(displayer+"|");
        }

        System.out.print("  |");
        for(int x = 0; x < width; x++){
          //Friggles
          String displayer1 = null;
          String side1 = null;
          if(areaGrid[x][y].plotFrigglePopulation() == 0){
            
            boolean edged = false;
            if(x==width-1){
              side1 = "|";
              edged = true;
            }else{
              side1 = side;
            }
            if(y==height-1){
              displayer1 = "_";
              side1 = "_";
              if(show==1){
                side1 = "|";
              }
              if(edged){
                side1 = "|";
              }
            }else{
              displayer1 = bottom;
            }
          }else{
            displayer1 = String.valueOf(areaGrid[x][y].plotFrigglePopulation());
            if(x==width-1){
              side1 = "|";
            }else{
              if(y==height-1){
                side1 = "_";
                if(show==1){
                  side1 = "|";
                }
              }else{
                side1 = side;
              }
            }
          }
          System.out.print(displayer1+side1);
        }
        System.out.print("  |");
        for(int x = 0; x < width; x++){
          String displayer1 = null;
          String side1 = null;

          //Bobbles
          if(areaGrid[x][y].plotBobblePopulation() == 0){
            
            boolean edged = false;
            if(x==width-1){
              side1 = "|";
              edged = true;
            }else{
              side1 = side;
            }
            if(y==height-1){
              displayer1 = "_";
              side1 = "_";
              if(show==1){
                side1 = "|";
              }
              if(edged){
                side1 = "|";
              }
            }else{
              displayer1 = bottom;
            }
          }else{
            displayer1 = String.valueOf(areaGrid[x][y].plotBobblePopulation());
            if(x==width-1){
              side1 = "|";
            }else{
              if(y==height-1){
                side1 = "_";
                if(show==1){
                  side1 = "|";
                }
              }else{
                side1 = side;
              }
            }
          }
          System.out.print(displayer1+side1);
        }
        System.out.println("");
      }
    }
  }

  public void setShow(int sh){
    show = sh;
  }
  
//////////////////////Time Passing/////////////////////
  /**
   *age all landplots
   */
  public void passTimeLandPlots(){
    for(LandPlot lp: easyAreaAccess){
      lp.age();
    }
  }
  
  /**
   *runs time step for all friggles
   */
  public void passTimeFriggles(){
    int index = 0; 
    //arrayList of arrayLists of incoming newborns
    ArrayList<ArrayList<Friggle>> babies = new ArrayList<ArrayList<Friggle>>();
    //arrayList of deceased friggles' positions in allFriggleList
    ArrayList<Integer> bodies = new ArrayList<Integer>();
    //goes through allFriggleList and does daily routine for all of them and adds their offspring to babies arraylist.
    for(Friggle josh: allFriggleList){
      if (josh.isAlive()){
        babies.add(josh.doDailyRoutine());
      }
      //newly deceased friggles' positions in allFriggleList are added to bodies ArrayList
      if (josh.isAlive()==false){
        bodies.add(index);
      }
      index += 1;
    }
    //bodies is reversed so that we can remove objects from higher indexes first. 
    //this way we prevent problems of index changes when removing friggles
    //remove all the dead friggles from allFriggleList
    Collections.reverse(bodies);
    for(Integer i: bodies){
      int b = (int) i;
      allFriggleList.remove(b);
    }
    //add babies to allFriggleList
    for(ArrayList<Friggle> b:babies){
      for(Friggle jill: b){
        allFriggleList.add(jill);
      }
    }
  }
  
  /**
   *runs Time stepfor all bobbles
   */
  public void passTimeBobbles(){
    //arrayList of arrayLists of incoming newborns
    ArrayList<ArrayList<Bobble>> babies = new ArrayList<ArrayList<Bobble>>();
    //arrayList of deceased bobbles' positions in allBobbleList
    ArrayList<Integer> bodies = new ArrayList<Integer>();
    //goes through allBobbleList and does daily routine for all of them and adds their offspring to babies arraylist.
    int index = 0;
    for(Bobble eric: allBobbleList){
      babies.add(eric.doDailyRoutine());
      if (eric.isAlive()==false){
        //newly deceased bobbles' positions in allBobbleList are added to bodies ArrayList
        bodies.add(index);
      }
      index++;
    }
    //bodies is reversed so that we can remove objects from higher indexes first. 
    //this way we prevent problems of index changes when removing bobbles
    //remove all the dead bobbles from allBobbleList
    Collections.reverse(bodies);
    for(Integer i: bodies){
      int b = (int) i;
      allBobbleList.remove(b);
    }
    //add babies to allBobbleList
    for(ArrayList<Bobble> b:babies){
      for(Bobble andrew: b){
        allBobbleList.add(andrew);
      }
    }
  }

  /**
   *Passes time for landplots,friggles,and bobbles.
   *displays environment in console
   *save population data
   *increments timeStep by one
   */
  public void passTime(){
    passTimeLandPlots();
    passTimeBobbles();
    passTimeFriggles();
    printAreaGrid();  
	
    System.out.println("\ntime elapsed:          "+timeStep);
    System.out.print("friggle population:    "+allFriggleList.size()+" ");
	printFriggleMutationAverages();
    System.out.print("bobble population:     "+allBobbleList.size()+" ");
	printBobbleMutationAverages();
	System.out.print("\n");
    savePopulations();
    timeStep+=1;
  }

///////////////////// Saving to File //////////////////////
  /**
   *Creates an array of length 4 and adds the relevant data to the array. then adds that array to the populations ArrayList
   */
  public void savePopulations(){
    int[] values = new int[4];
    int g = 0;
    for(LandPlot b: easyAreaAccess){
      if(b.getBeeterWeedStatus()){
        g++;
      }
    }
    values[0]= timeStep;
    values[1]= g;
    values[2]= allFriggleList.size();
    values[3]= allBobbleList.size();
    populations.add(values);

  }
  
  /**
   *Creates a properly numbered new file called populations_.txt where _  is an ever increasing number
   */
  public void saveToFile(){
    try {
      String fileNamer = "populations0.txt";
      boolean avaliable = false;
      FileWriter myWriter = null;
      while(avaliable == false){
        File filer = new File(fileNamer);
        //if a file by the name fileNamer already exists. break apart fileNamer and increment the number between the "s" and the "." in the name up by one. then try again
        if(filer.exists()){
        //add count
          int num = Integer.parseInt(fileNamer.split("\\.")[0].split("s")[1]);
          num += 1;
          String inserter = String.valueOf(num);
          String newName = fileNamer.split("\\.")[0].split("s")[0] + "s"+ inserter +"." +fileNamer.split("\\.")[1];
          fileNamer = newName;
        }else{
          //you get here by finding a file name that is not in use.
          //create a fileWriter for that fileName
          avaliable = true;
          myWriter = new FileWriter(fileNamer);
          System.out.println("new file "+fileNamer+ " created!");
        }
      }
      //Write information stored in populations arrayList into data file.
      myWriter.write("Day,Beeterweed,Friggle,Bobble\n");
      for(int[] b:populations){
        for(int i: b){
          myWriter.write(i+",");
        }
        myWriter.write("\n");
      }
      myWriter.close();
      System.out.println("saved");
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }
  }
  
///////////////////// Yellowstone /////////////////////
  /**
   *kill all bobbles
   */
  public void exterminateBobbles(){
    for(Bobble jeff: allBobbleList){
      jeff.die();
    }
    allBobbleList.clear();
  }
  
  
  
/////////////////////Printing Out Mutation Info/////////////////
	public void printFriggleMutationAverages(){
		float count = 0;
		float frigMaxAgeSum = 0;
		for (Friggle james: allFriggleList){
			frigMaxAgeSum += (float) james.getMaxAge();
			count++;
		}
		float aveFrigMaxAge = frigMaxAgeSum/count;
		System.out.print("Friggle Averages: Max Age:");
		System.out.println(aveFrigMaxAge);
		
	}
	
	public void printBobbleMutationAverages(){
		float count = 0;
		float bobMaxAgeSum = 0;
		float bobBabyCountSum = 0;
		float bobRTSum = 0;
		float bobMoveCountSum = 0;
		for (Bobble james: allBobbleList){
			bobMaxAgeSum += (float) james.getMaxAge();
			bobBabyCountSum += (float) james.getBabyCount();
			bobRTSum += (float) james.getReproductionEnergyThreshold();
			bobMoveCountSum += (float) james.getMoveCount();
			count++;
		}
		float aveBobMaxAge = bobMaxAgeSum/count;
		float aveBobBabyCount = bobBabyCountSum/count;
		float aveBobRT = bobRTSum/count;
		float aveBobMoveCount = bobMoveCountSum/count;
		
		System.out.print("Bobbles Averages: Max Age: " + aveBobMaxAge +"  ");
		System.out.print("Litter Size:" + aveBobBabyCount + "  ");
		System.out.print("Reproduction Threshold:" + aveBobRT + "  ");
		System.out.print("Move Count:" + aveBobMoveCount + "  ");
		
	}
//////////////////// Main (operate simulation) ///////////////
  public static void main(String[] args) {
    // create sim
    Simulation sim1 = new Simulation(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
    // create landplots
    sim1.fillAreaGrid();
    // add frigs
    sim1.addFriggles(Integer.parseInt(args[2]));
    // add bobbs
    sim1.addBobbles(Integer.parseInt(args[3]));
    // set display style
    sim1.setShow(Integer.parseInt(args[4]));
    // print areaGrid once
    sim1.printAreaGrid();
    int cycles = 1000;
    int click;
    //Run simulation with user
    Scanner clickStep = new Scanner(System.in);
    for(int i = 0; i <= cycles; i++ ){
      System.out.println("Timesteps to simulate (or save):");
      String f = clickStep.next();
      if (!f.equals("save")){
        //Yellowstone options 
        if (f.equals("exterminateBobbles")){
          sim1.exterminateBobbles();
          click = 1;
        }else if (f.equals("reintroduceBobbles")){
          sim1.addBobbles(1);
          click=1;
        }else{
          click = (int) Integer.parseInt(f);
        }
        
        for( int b = 0; b < click; b++){
        sim1.passTime();
		
        }
      }else{
        //save 
        sim1.saveToFile();
		
        clickStep.close();
        
        
      }
    }
  }
}
import java.util.*;

public class FrigglePQ implements PriorityQueue<Friggle>{
//////////////////////////////Variables//////////////////////////////
  //List containing all friggles at one landplot
  ArrayList<Friggle> landPlotFriggleList = new ArrayList<Friggle>();
  //size of landPlotFriggleList
  private int size;
///////////////////////////////other methods////////////////////////
  /**
   *Returns the list (PQ)
   *@return landPlotFriggleList ArrayList<Friggle> - PQ
   */
  public ArrayList<Friggle> getList(){
    return landPlotFriggleList;
  }
  
  /**
   *Constructor for object
   */
  public FrigglePQ(){
    size = 0;
  }

  /**
   *Returns true if self is leaf i and false if not
   *@param position int - own location in list
   *@return n/a boolean - isLeaf status 
   */
  public boolean isLeaf(int position){
    return (position >= size/2) && (position<size);
  }

  /**
   *looks for left child of current node
   *@param position int - of self
   *@return position int - of left child in PQ
   */
  public int leftChild(int position){
    if (position>= size/2){
      return -1;
    }else{
      return 2*position + 1;
    }
  }
  
  /**
   *Finds position of parent
   *@param position int - of self
   *@return position int - of parent
   */
  public int parent(int position){
    if (position <= 0){
      return -1;
    }else{
      return (position-1)/2;
    }
  }
  
  /**
   *adds a friggle to the PQ and does neccessary swaps
   *@param angela Friggle - friggle to be added to list
   */
  public void add(Friggle angela){
    landPlotFriggleList.add(angela);
    size += 1;
    int curr = size -1;
    while((curr != 0) && (landPlotFriggleList.get(curr).compareTo(landPlotFriggleList.get(parent(curr)))  < 0)){
      Collections.swap(landPlotFriggleList, curr, parent(curr));
      curr = parent(curr);
    }
  }

  /**
   *builds up PQ list
   */
  public void buildlandPlotFriggleList(){
    for(int i = size/2-1; i>= 0 ;i--){
      siftdown(i);
    }
  }
  
  /**
   *Sifts down from a certain starting position to reorginaize PQ
   *@param position int - starting position for sift
   *@return is used only to exit loop
   */
  public void siftdown(int position){
    if ((position < 0) || (position >= size)){
      return;
    }else{
      while(isLeaf(position)==false){
        int j = leftChild(position);
        if ((j < size-1) && (landPlotFriggleList.get(j).compareTo(landPlotFriggleList.get(j+1)) > 0)){
          j++;
        }
        if(landPlotFriggleList.get(position).compareTo(landPlotFriggleList.get(j)) <= 0){
          return;
        }else{
          Collections.swap(landPlotFriggleList, position, j);
          position = j;
        }
      }
    }
  }

  /**
   *Removes a friggle at certain position from the PQ
   *@param pos int - position in PQ of friggle
   *@return Friggle - returns the last friggle from the list whilst removing it
   */
  public Friggle remove(int pos) {
    if ((pos < 0) || (pos >= size)){
      return null;
    }
    if (pos == (size-1)){
      size--;
    } else {
      Collections.swap(landPlotFriggleList, pos, size -1); 
      size -= 1;
      update(pos);
    }
    return landPlotFriggleList.remove(size);
  }

  /**
   *Removes and Returns the top friggle in pq
   *@return tem Friggle - the friggle that was taken off the front of PQ
   */
  public Friggle poll(){
    if (size == 0){
      throw new NoSuchElementException();
    }
    Collections.swap(landPlotFriggleList, 0, size-1);
    Friggle tem = landPlotFriggleList.get(size-1);
    landPlotFriggleList.remove(size-1);
    size -= 1;
    siftdown(0);
    return tem;
  }
  
  /**
   *Returns top priority friggle
   *@return b Friggle - friggle at the 0th position in PQ
   */
  public Friggle peek(){
    if (isEmpty()){
      throw new NoSuchElementException();
    }else{
      return landPlotFriggleList.get(0);
    }
  }

  /**
   *Returns true if PQ is empty, false if not
   *@return boolean - whether list is empty or not
   */
  public boolean isEmpty(){
    if(size == 0){
      return true;
    }else{
      return false;
    }
  }
  
  /**
   *clears the PQ for the LandPlot
   */
  public void clear(){
    landPlotFriggleList.clear();
    size = 0;
  }
  
  /**
   *updates the PQ at pos
   *@param pos int - point to start updating from
   */
  void update(int pos) {
    while ((pos > 0) && (landPlotFriggleList.get(pos).compareTo(landPlotFriggleList.get(parent(pos))) < 0)){
      Collections.swap(landPlotFriggleList, pos, parent(pos));
      pos = parent(pos);
    }
    siftdown(pos);
  }
}

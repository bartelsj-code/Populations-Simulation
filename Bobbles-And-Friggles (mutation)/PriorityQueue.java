import java.util.*;
// this was taken directly from HW-8. No changes have been made and it is not my code.
public interface PriorityQueue<T extends Comparable<? super T>> {
    
    /** Adds the given item to the queue. */
    public void add(T item);
    
    /** Removes the first item according to compareTo from the queue, and returns it.
     * Throws a NoSuchElementException if the queue is empty.
     */
    public T poll();
    
    /** Returns the first item according to compareTo in the queue, without removing it.
     * Throws a NoSuchElementException if the queue is empty.
     */
    public T peek();
    
    /** Returns true if the queue is empty. */
    public boolean isEmpty();
    
    /** Removes all items from the queue. */
    public void clear();
}

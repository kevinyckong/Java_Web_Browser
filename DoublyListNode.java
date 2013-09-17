/**
 * 
 * This class creates nodes for a doubly linked list data structure. Each node
 * contains a string value, as well as references to a previous and a next node
 * of the same class.  This allows the user to create a dynamic data structure.
 * 
 * @author Kevin Kong
 * @version May 30, 2011
 * 
 */
class DoublyListNode 
{
  //creates instance variables
  private String value; //value to be held
  private DoublyListNode next; //reference to the next node
  private DoublyListNode previous; //reference to the previous node
  
  /**
   * This is a constructor for the class, which takes in a String parameter.
   * This constructor will call the overloaded constructor with all the
   * references and value of the node to create a DoublyListNode.
   * 
   * @param nodeValue The String value to be held by the node.
   */
  public DoublyListNode (String nodeValue) 
  {
    //calls the overloaded constructor to create the node
    this( null, nodeValue, null);
  }
  
  /**
   * This is the overloaded constructor which takes in two references values as well
   * as in a String parameter.  This constructor will then set the instance variables to
   * the values as specified by the parameters.
   * 
   * @param nodeBefore A reference to the previous node.
   * @param nodeValue The String value to be held by the node.
   * @param nodeNext A reference to the next node.
   */
  public DoublyListNode (DoublyListNode nodeBefore, String nodeValue, DoublyListNode nodeNext) 
  {
    value = nodeValue;
    next = nodeNext;
    previous = nodeBefore;
  }
  
  /**
   * This is the accessor method for the instance variable next. This method takes no 
   * parameters and returns the reference held by the current node object to the next.
   * 
   * @return The reference to the next node.
   */
  public DoublyListNode getNext () 
  {
    return next;
  }
  
  /**
   * This is the accessor method for the instance variable previous. This method takes no 
   * parameters and returns the reference held by the current node object to the previous.
   * 
   * @return The reference to the next node.
   */
  public DoublyListNode getPrevious ()
  {
    return previous;
  }
  
  /**
   * This is the accessor method for the instance variable value. This method takes no 
   * parameters and returns the String value of the current node object.
   * 
   * @return The String object held by the class.
   */
  public String getValue () 
  {
    return value;
  }
  
  /**
   * This is the mutator method for the instance variable next.  This method takes in a
   * DoublyListNode parameter and assigns the reference to the instance variable next.
   * 
   * @param newNext The new reference to be assigned to the instance variable next.
   */
  public void setNext( DoublyListNode newNext ) 
  {
    next = newNext;
  }
  
  /**
   * This is the mutator method for the instance variable previous.  This method takes in a
   * DoublyListNode parameter and assigns the reference to the instance variable previous.
   * 
   * @param newPrevious The new reference to be assigned to the instance variable next.
   */
  public void setPrevious (DoublyListNode newPrevious)
  {
    previous = newPrevious;
  }
  
  
  /**
   * This is the mutator method for the instance variable value.  This method takes in a
   * String parameter and assigns the value of that parameter to the instance variable value.
   * 
   * @param newValue The new value to be assigned to the instance variable value.
   */
  public void setValue (String newValue) 
  {
    value = newValue;
  }
}

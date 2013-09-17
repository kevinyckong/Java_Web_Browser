/**
 * 
 * This class creates a doubly linked list data structure by using DoublyListNodes.
 * The object created will have be a dynamic data structure, and allow the user to
 * add an infinite number of String elements.
 * 
 * @author Kevin Kong
 * @version May 30, 2011
 * 
 */
class DoublyLinkedList 
{
  //creates instance variables
  private DoublyListNode head; //first element of the structure
  private DoublyListNode tail; //last element of the structure
  
  
  /**
   * This is the constructor for the class, which takes no parameters.
   * This constructor will set the 'head' and 'tail' reference variables to null.
   * 
   */
  public DoublyLinkedList () 
  {
    head = null;
    tail = null;
  }
  
  
  /**
   * This method takes in a String parameters and returns no values.  This method
   * will add an DoublyListNode element to the end of this data structure, which 
   * will contain the value of the String parameter passed. 
   * 
   * @param item The value of the DoublyListNode to be added to the end of the structure.
   */
  public void addEnd (String item)
  {
    //if the structure is empty
    if (head == null)
    {
      //sets the tail to point to a new DoublyListNode with the value of the parameter
      tail = new DoublyListNode (tail, item, null);
      //sets the head to point to the same DoublyListNode object
      head = tail;
    }
    //else if the structure is not empty
    else
    {
      //creates a new node with the value of the parameter
      DoublyListNode newNode = new DoublyListNode (tail, item, null);
      //calls the setNext method in the current tail to reference the new node
      tail.setNext(newNode);
      //sets the new tail as the new node created
      tail = newNode;
    }
  }
  
  
  /**
   * This method takes in a String parameters and returns no values.  This method
   * will add an DoublyListNode element to the front of this data structure, which 
   * will contain the value of the String parameter passed. 
   * 
   * @param item The value of the DoublyListNode to be added to the front of the structure.
   */
  public void addFront (String item) 
  {
    //if the list is empty
    if (head == null)
    {
      //sets the head to reference a DoublyListNode with the String parameter value
      head = new DoublyListNode( null, item, head );
      //sets the tail to reference the same DoublyListNode
      tail = head;
    }
    //else if the list is not empty
    else
    {
      //sets the head to reference a DoublyListNode with the String parameter values
      //and which points to the previous head
      head = new DoublyListNode( null, item, head );
    }
  }
  
  
  /**
   * This method will delete a node in the list with a given String value parameter.
   * This method will return true if the node is found and deleted, or false if not.
   * 
   * @param key The value to search for.
   * @return True if the node is found and deleted, or false if not.
   */
  public boolean delete (String key) 
  {
    //creates variables to help with the search and delete
    DoublyListNode before = null; //the node before
    DoublyListNode current = head; //the current node
    
    //while loop
    //loops if the current node is not null (not completely traversed the list)
    //or if the current node does not contain the save value as the key
    while ((current != null) && (!key.equals(current.getValue())) ) 
    {
      //sets the node before to point to the previous current object
      before = current; 
      //sets the current node to point to the next object
      current = current.getNext();
    }
    
    //return false if the key was not found in the linked list
    if (current == null) 
    {
      return false;
    }
    
    //Adjust head of the list or set before's link to point to the node after current, as appropriate
    //if the node before the current node is null (the current node is the first node)
    if (before == null) 
    {
      //sets head to point to the next node of the current node
      head = current.getNext();
    }
    //else if the current node is the last node (tail)
    else if (current.getNext() == null)
    {
      //sets the next node of the node before to point to null
      before.setNext(current.getNext());
      //sets the tail to reference the node before
      tail = before;
    }
    //else if the current node is within the list
    else 
    {
      //set the next reference of the node before to the current node's next value
      before.setNext(current.getNext());
      //sets the previous reference of the node after the current node to the node before the current node
      current.getNext().setPrevious(before);
    }
    //return true
    return true;
  }
  
  
  /**
   * This method returns the DoublyListNode that is at the head position of the data structure.
   * 
   * @return The DoublyListNode item occupying the head position of the structure.
   */
  public DoublyListNode getHead ()
  {
    return head;
  }
  
  
  /**
   * This method will return the String value of a node that is the next node
   * of a node containing a specified String parameter value. 
   * 
   * @param current The value of the current node.
   * @return The value of the current node's next node.
   */
  public String getNext (String current)
  {
    //searches the list to find the node containing the parameter value
    DoublyListNode currentNode = searchByValue(current);
    //returns the value of the node that is the next of the current node
    return currentNode.getNext().getValue();
  }
  
  
  /**
   * This method will return the String value of a node that is the previous node
   * of a node containing a specified String parameter value. 
   * 
   * @param current The value of the current node.
   * @return The value of the current node's previous node.
   */
  public String getPrevious (String current)
  {
    //searches the list to find the node containing the parameter value
    DoublyListNode currentNode = searchByValue(current);
    //returns the value of the node that is the previous of the current node
    return currentNode.getPrevious().getValue();
  }
  
  
  /**
   * This method returns the DoublyListNode that is at the tail position of the data structure.
   * 
   * @return The DoublyListNode item occupying the tail position of the structure.
   */
  public DoublyListNode getTail ()
  {
    return tail;
  }
  
  
  /**
   * This method takes in one String parameter, and checks to see if the node
   * containing the value of that parameter points to a next DoublyListNode.
   * 
   * @param current The value of the current node.
   * @return True if the current node points to a next DoublyListNode, false if not.
   */
  public boolean hasNext (String current)
  {
    //searches the list to find the node containing the parameter value
    DoublyListNode currentNode = searchByValue(current);
    //if the search fails and the parameter value is not found
    if (currentNode == null)
    {
      return false;
    }
    //else if the parameter is found
    else
    {
      //if the current node does not reference a next node
      if (currentNode.getNext() == null)
      {
        return false;
      }
      //else if the current node does reference a next node
      else
      {
        return true;
      }
    }
  }
  
  
  /**
   * This method takes in one String parameter, and checks to see if the node
   * containing the value of that parameter points to a previous DoublyListNode.
   * 
   * @param current The value of the current node.
   * @return True if the current node points to a previous DoublyListNode, false if not.
   */
  public boolean hasPrevious (String current)
  {
    //searches the list to find the node containing the parameter value
    DoublyListNode currentNode = searchByValue(current);
    //if the search fails and the parameter value is not found
    if (currentNode == null)
    {
      return false;
    }
    //else if the parameter is found
    else
    {
      //if the current node does not reference a previous node
      if (currentNode.getPrevious() == null)
      {
        return false;
      }
      //else if the current node does referencing a previous node
      else
      {
        return true;
      }
    }
  }
  
  
  /**
   * This method takes no parameters, and returns a boolean value depending on if
   * the data structure is empty.
   *
   * @return True if the list is empty, and false if it is not.
   */
  public boolean isEmpty () 
  {
    return (head == null);
  }
  
  /**
   * This method takes no parameters, and returns no values. This method will
   * clear the data structure of all elements and make it empty.
   */
  public void makeEmpty () 
  {
    //sets the first and last elements to point to null
    head = null;
    tail = null;
  }
  
  
  /** 
   * This method removes a node from the tail of the list and returns a reference
   * to the node's value.  This method takes no parameters and will return null if
   * the list is empty.
   * 
   * @return The value of the DoublyListNode that is deleted.
   */
  public String removeEnd () 
  {
    String tempValue;
    
    //if the list is empty then return null
    if (isEmpty())
      return null;
    
    //stores the value of the tail
    tempValue = tail.getValue();
    //sets the new tail to the previous node of the original tail
    tail = tail.getPrevious();
    //if the tail is null
    if (tail == null)
    {
      //set head to null
      head = null;
    }
    //else if the tail is not null (list is not empty)
    else
    {
      //sets the next node reference of the new tail to null
      tail.setNext(null);
    }
    //return value of the original tail
    return tempValue;
  }
  
  
  /** 
   * This method removes a node from the head of the list and returns a reference
   * to the node's value.  This method takes no parameters and will return null if
   * the list is empty.
   * 
   * @return The value of the DoublyListNode that is deleted.
   */
  public String removeFront ()
  {
    String tempValue;
    
    //if the list is empty then return null
    if (isEmpty()) 
      return null;
    
    //gets the value of the current head
    tempValue = head.getValue();
    //change current head to the next node
    head = head.getNext();
    //changes the current head to refer to null for previous node
    head.setPrevious(null);
    //if the current head is null
    if (head == null)
    {
      //set tail to null
      tail = null;
    }
    //return the value of the original head
    return tempValue;
  }
  
  
  /**
   * This method will search for an item in the list containing the value
   * of the String parameter.  This method will return the reference to the
   * DoublyListNode if it is found, or null otherwise.
   * 
   * @param key The value to search for.
   * @return The reference to the node containing the value, or null if not found.
   */
  public DoublyListNode searchByValue (String key) 
  {
    //creates a node to search with
    DoublyListNode searchNode;
    //sets the search node to point to the head of the list
    searchNode = head;
    //while loop
    //loops if the value of the search node is not equal to the desired value
    //and if the search node is not null (have not finished traversing the list)
    while ((searchNode != null) && (!key.equals(searchNode.getValue())) ) 
    {
      //set search node to point to the next node
      searchNode = searchNode.getNext();
    }
    //return the reference of the search node
    return searchNode;
  }
  
  // Return String representation of the linked list.
  public String toString () 
  {
    DoublyListNode node;
    String linkedList = "HEAD ==> ";
    
    node = head;
    while (node != null) 
    {
      linkedList += "[ " + node.getValue() + " ] ==> ";
      node = node.getNext();
    };
    
    return linkedList + "NULL";
  }
}

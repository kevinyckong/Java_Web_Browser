//imports necessary classes
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * This class extends the JScrollPane class and contain variables
 * and methods for the user retrieve and display URL's in the area
 * of the object. This class contains a JEditorPane to retrieve and
 * display URL's, and a DoublyLinkedList structure to keep history.
 *
 * @author Kevin Kong
 * @version May 30, 2011
 */
public class WebPage extends JScrollPane
{
  //instance variables
  private JEditorPane displayPane; //the area to display the web content
  private DoublyLinkedList history; //to keep track of the visited pages
  private DoublyListNode currentPage; //the current page displayed
  private boolean replaceHistory; //to indicate if to overwrite history
  
  
  /**
   * This is the constructor for the class.  This constructor
   * will initialize all instance variables and set the JEditorPane content pane
   * to appear inside the scroll pane.
   */
  public WebPage ()
  {
    displayPane = new JEditorPane ();
    history = new DoublyLinkedList ();
    currentPage = new DoublyListNode ("New Page");
    replaceHistory = false;
    
    //locks the displayPane so users cannot change it
    displayPane.setEditable (false);
    //creates a viewport and set the displayPane as the displayed content area for the scroll pane
    setViewportView (displayPane);
  }
  
  
  /**
   * This is the accessor method for the instance variable currentPage.
   * 
   * @return The string value contained by the variable currentPage.
   */
  public String getCurrentPage ()
  {
    return currentPage.getValue ();
  }
  
  
  /**
   * This is the accessor method for the instance variable displayPane.
   *
   * @return The displayPane from the current class.
   */
  public JEditorPane getEditorPane ()
  {
    return displayPane;
  }
  
  
  /**
   * This method determines if there is another DoublyListNode associated
   * with and after the currentPage variable.
   * 
   * @return True if currentPage contains a reference to the next object, false if not.
   */
  public boolean hasNextPage ()
  {
    //if currentPage does not contain a reference for a next object
    if (currentPage.getNext () == null)
    {
      //return false
      return false;
    }
    //else if currentPage is referring to a next object
    else
    {
      //return true
      return true;
    }
  }
  
  
  /**
   * This method determines if there is another DoublyListNode associated
   * with and previous to the currentPage variable.
   * 
   * @return True if currentPage contains a reference to a previous object, false if not.
   */
  public boolean hasPreviousPage ()
  {
    //if currentPage does not contain a reference for a previous object
    if (currentPage.getPrevious () == null)
    {
      //return false
      return false;
    }
    //else if currentPage does contain a reference for a previous object
    else
    {
      //return true
      return true;
    }
  }
  
  
  /**
   * This method sets the content pane to display the website provided by the URL
   * that is after the currentPage in the history.
   */
  public void setNextPage ()
  {
    //try and catch statement to set the page
    try 
    {
      //set currentPage to point to the reference to the next DoublyListNode object
      currentPage = currentPage.getNext ();
      //set the page using the URL of the currentPage
      displayPane.setPage (currentPage.getValue() );
    }
    //if there is an error in displaying the URL
    catch (IOException ioException) 
    {
      //outputs error message
      JOptionPane.showMessageDialog (this,"Error retrieving specified URL", "Bad URL", 
                                     JOptionPane.ERROR_MESSAGE);
    }
  }
  
  
  /**
   * This method sets the content pane to display the website provided by the URL
   * that is previous to the currentPage in the history.
   */
  public void setPreviousPage ()
  {
    //try and catch statement to set the page
    try 
    {
      //set currentPage to point to the reference to the previous DoublyListNode object
      currentPage = currentPage.getPrevious ();
      //set the page using the URL of the currentPage
      displayPane.setPage (currentPage.getValue() );
      //changes replaceHistory to true
      //when user goes back in the history and then enters a new address,
      //the old history will be replaced with the new address
      replaceHistory = true;
    }
    //if there is an error in displaying the URL
    catch (IOException ioException) 
    {
      //outputs error message
      JOptionPane.showMessageDialog (this,"Error retrieving specified URL", "Bad URL", 
                                     JOptionPane.ERROR_MESSAGE);
    }
  }
  
  
  /**
   * This method takes in a String URL parameter, and sets the content pane of
   * the displayPane to display the webpage specified by the URL.  This method
   * will also adjust the position of currentPage in the list of history, as wel
   * as the history itself.
   *
   * @param location The URL to be displayed.
   */
  public void setThePage (String location)
  {
    //try and catch statement to set the page
    try
    {
      //set the displayPane to display the URL page
      displayPane.setPage (location);
      //if the specified page is not the same as the current page
      //eg. not refreshing or entering the same website twice
      if (!location.equals(currentPage.getValue()) )
      {
        //if the history should not be replaced
        if (!replaceHistory)
        {
          //add the URL to the end of the history
          history.addEnd (location);
        }
        //else if the history should be replaced
        else
        {
          //while loop
          //while the currentPage has references pointing to next DoublyListNode in the history
          while (currentPage.getNext() != null)
          {
            //remove the last DoublyListNode from the end of the history
            //replacing and erasing the previous pass
            history.removeEnd ();
          }
          //add the current URL to the end of the history list
          history.addEnd (location);
          //change replaceHistory to false
          //because now the displayed page is the end of the list
          replaceHistory = false;
        }
      }
      //else if the specified page is the same as the current page displayed
      else
      {
        //if there are other DoublyListNodes after currentPage in the history
        if (history.hasNext(currentPage.getValue()) )
        {
          //while the currentPage has references pointing to next DoublyListNode in the history
          while (currentPage.getNext() != null)
          {
            //remove the last DoublyListNode from the end of the history
            //replacing and erasing the previous pass
            history.removeEnd ();
          }
          //add the current URL to the end of the history list
          history.addEnd (location);
        }
      }
      
      //if the history list is not empty
      if (!history.isEmpty() )
      {
        //set the currentPage to the last element of the history list
        currentPage = history.getTail ();
      }
      //if the history list is empty
      else
      {
        //set the currentPage to the first element of the history list
        currentPage = history.getHead ();
      }
    }
    
    //if there is an IOException when trying to set the page
    catch (IOException ioException) 
    {
      //outputs an error message
      JOptionPane.showMessageDialog (this,"Error retrieving specified URL", "Bad URL", 
                                     JOptionPane.ERROR_MESSAGE);
    }
  }
}
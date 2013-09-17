//import necessary classes
//from awt package
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//from swing package
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
//from util package
import java.util.Random;
import java.util.Scanner;
//from io package
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


/**
 * This class creates a web browser and allows the user to surf the internet.
 * There are many 
 * 
 * @author Kevin Kong
 * @version June 05, 2011
 */
public class KingKongBrowser extends JFrame 
{
  //instance variables
  private JTextField addressEnterField; //textfield for user to enter the website address
  private JButton forwardButton; //button to go to the next page
  private JButton backButton; //button to go back to previous page
  private JButton homeButton; //home button
  private JPanel topPanel; //the panel containing the textfield and buttons which is going to be at the top of the display
  private JTabbedPane webPageTabPane; //for tabbed browsing
  private WebPage currentWebPage; //reference to the current selected WebPage object
  private int totalTabNum; //index of the current tab
  private JMenu historyMenu; //menu to display past visited sites
  private static Color [] tabColors = {Color.RED, Color.YELLOW, Color.ORANGE, Color.BLUE, Color.GREEN, Color.PINK, Color.MAGENTA,
    Color.LIGHT_GRAY, Color.CYAN}; //the colors possible for each of the tabs
  private Random randomColor; //to generate a random color to assign to each tab
  private int numInHistory; //the number of items in the history
  private DoublyLinkedList masterHistory; //a complete list of all sites visited by all tabs
  
  private PreferenceWindow preferenceWindow; //a preference window
  private String homePage; //home page setting
  private String pLAF; //the pluggable look and feel of the browser
  private int historySize; //size of the history
  private boolean changePLAF; //to specify when to change the look and feel
  
  //constants to be used
  private final static int ACCEPTEDTABTITLESIZE = 20; //maximum length of the title of the tabs
  private final static int ACCEPTEDMENUITEMSIZE = 40; //maximum length of the items in the history menu
  private final static int TAB = 0; //specifies a tab object
  private final static int MENUITEM = 1; //specifies a menu item object
  private final static int MINIMUMHISTORYSIZE = 2; //the minimum size of the history menu
  private final static int VALIDURLTITLE = 7; //least number of characters in the title of a valid URL "http://"
  private final static int TERMINATEPROGRAM = 0; //forcibly exit the program
  private final static int MAXIMUMHISTORYSIZE = 30; //maximum number of items in the history for good style display
  private final static int OLDESTHISTORY = 2; //the position of the oldest site visited in the history menu
  
  
  /**
   * This is the constructor for the class.  This constructor will initialize all variables
   * and construct a user-friendly GUI.  This constructor will add a tab pane and menubar to
   * the basic frame.
   */
  public KingKongBrowser()
  {
    //calls the constructor in the parent class to set the title of the window
    super("KingKong Browser");
    
    //initialize variables
    changePLAF = true;
    totalTabNum = -1; 
    randomColor = new Random ();
    masterHistory = new DoublyLinkedList ();
    preferenceWindow = new PreferenceWindow ();
    forwardButton = new JButton ("Forward");
    backButton = new JButton ("Back");
    topPanel = new JPanel ();
    currentWebPage = new WebPage ();
    historyMenu = new JMenu ("History");
    homeButton = new JButton ("Home");
    webPageTabPane = new JTabbedPane ();
    addressEnterField = new JTextField (currentWebPage.getCurrentPage(), ACCEPTEDTABTITLESIZE);
    
    //adds a window listener to the preferenceWindow to monitor when it is no longer visible
    preferenceWindow.addWindowListener (new WindowHandler() );
    //adds event handler to each of the buttons
    homeButton.addActionListener (new ButtonHandler() );
    backButton.addActionListener (new ButtonHandler() );
    forwardButton.addActionListener (new ButtonHandler() );
    //adds event handler to the text field for when the user enters a URL
    addressEnterField.addActionListener (new TextFieldHandler() );
    
    //calls the createMenuBar method to create a JMenuBar and set it as the menubar of this window
    setJMenuBar (createMenuBar());
    
    //set scroll layout to the tab pane
    webPageTabPane.setTabLayoutPolicy (JTabbedPane.SCROLL_TAB_LAYOUT);
    //adds a first tab to the tab pane
    addTab();
    //adds event handler to the tab pane to monitor when the user changes from tab to tab
    webPageTabPane.addChangeListener (new TabChangeListener());
    
    //sets the layout of the topPanel to GridBagLayout
    topPanel.setLayout (new GridBagLayout() );
    //create a GridBagConstranits object to help with laying out the items
    GridBagConstraints constraints = new GridBagConstraints();
    //changes the constraint for the first item
    constraints.weightx = 0.5; //weight or proportion of the item in the row
    constraints.fill = GridBagConstraints.BOTH; //resize to fill both directions
    constraints.gridx = 0; //first item
    constraints.gridy = 0; //first line
    constraints.insets = new Insets (5,5,5,5); //adds padding between the items
    //adds the backButton to the panel with the specified constraints
    topPanel.add (backButton, constraints); 
    
    //changes the constraints for the second object
    constraints.gridx = 1; //first line second item from the left
    //adds the forwardButton to the topPanel with the specified constraints
    topPanel.add (forwardButton, constraints); 
    
    //changes the constraints for the third object
    constraints.weightx = 6; //more important than the other buttons, take up more horizontal space
    constraints.gridx = 2; //third item in the first line
    //adds the text field to the topPanel with the specified constraints
    topPanel.add (addressEnterField, constraints); 
    
    //changes te constraints for the fourth object
    constraints.weightx = 0.5; //same weighting as the other two buttons
    constraints.gridx = 3; //fourth item in the first row
    //adds the home button to the topPanel with the specified constraints
    topPanel.add (homeButton, constraints); 
    
    //adds the topPanel to the North of the window
    add (topPanel, BorderLayout.NORTH);
    
    //calls the setPreferences method to load and configure window preferences
    setPreferences ();
    
    //adds the tabbed pane to the window
    add (webPageTabPane);
    //calls the setHomePage method to load the saved home page
    setHomePage();
  }
  
  /**
   * This method will add a new tab to the tabbed pane with a new WebPage as
   * the Component in the window.
   */
  private void addTab ()
  {
    //creates a new WebPage object and assigns it to the currentWebPage
    //when open a new tab the new tab is selected
    currentWebPage = new WebPage ();
    //adds the event handler to the JEditorPane of the WebPage object
    currentWebPage.getEditorPane().addHyperlinkListener (new HyperlinkHandler() );
    
    //calls the getCurrentPage method in currentWebPage to get the appropriate title for the tabbed pane
    //adds a tab to the tabbed pane with the title and the currentWebPage as the Component
    webPageTabPane.addTab (currentWebPage.getCurrentPage(), currentWebPage);
    //increase the number of total tabs
    totalTabNum ++;
    //set the tabbed pane to select the newly created tab
    webPageTabPane.setSelectedIndex (totalTabNum);
    //sets the background color to the newly created tab with the Color object located 
    //at a random element generated by randomColor
    webPageTabPane.setBackgroundAt (totalTabNum, tabColors [randomColor.nextInt(tabColors.length)]);
  }
  
  /**
   * This method takes in a String parameter, and checks to see if it is a valid
   * URL of if it is not.  This method will then return a boolean value based on the decision.
   * 
   * @param location The URL to be tested/validated.
   * @return True if the URL is valid, false otherwise.
   */
  private boolean checkValidPage (String location)
  {
    //try and catch statement to set the URL page
    try
    {
      //creates a new JEditorPane object to test the URL
      JEditorPane testPane = new JEditorPane ();
      //set the test pane to display the URL contents
      testPane.setPage (location);
      //if there are no errors, return true
      return true;
    }
    //catches if there is an IOException error when setting the page
    catch (IOException ioException)
    {
      //returns false
      return false;
    }
  }
  
  /**
   * This method will create and return a menu bar specific to the needs
   * of this class.
   * 
   * @return The formatted and assembled menu bar.
   */
  private JMenuBar createMenuBar ()
  {
    //creates a JMenuBar
    JMenuBar menuBar = new JMenuBar ();
    
    //creates a menu named File
    JMenu fileMenu = new JMenu ("File");
    //creates a menu item named New Tab
    JMenuItem newTabItem = new JMenuItem ("New Tab");
    //adds event handler to the menu item
    newTabItem.addActionListener (new MenuItemHandler() );
    //set accelerator keys to Alt and N
    newTabItem.setAccelerator ((KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK)));
    //sets tool tip hints
    newTabItem.setToolTipText ("Opens a new tab in the browser");
    //adds the newTabItem to the File menu
    fileMenu.add (newTabItem);
    
    //creates a menu item named Delete Tab
    JMenuItem deleteTabItem = new JMenuItem ("Delete Tab");
    //adds event handler to the item
    deleteTabItem.addActionListener (new MenuItemHandler() );
    //set accelerator keys to Alt and C
    deleteTabItem.setAccelerator ((KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK)));
    //sets tool tip hints
    deleteTabItem.setToolTipText ("Closes the current tab");
    //adds the deleteTabItem to the File menu
    fileMenu.add (deleteTabItem);
    
    //adds a separator to the File menu
    fileMenu.addSeparator ();
    //creates a menu item named Exit Browser
    JMenuItem exitItem = new JMenuItem ("Exit Browser");
    //adds event handler to the item
    exitItem.addActionListener (new MenuItemHandler ());
    //set accelerator keys to Alt and E
    exitItem.setAccelerator ((KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK)));
    //sets tool tip hints
    exitItem.setToolTipText ("Exits the program");
    //adds the exitItem to the File menu
    fileMenu.add(exitItem);
    
    //adds the File menu to the menu bar
    menuBar.add (fileMenu);
    
    //creates a menu named Edit
    JMenu editMenu = new JMenu ("Edit");
    //creates a menu item named Preferences
    JMenuItem preferencesItem = new JMenuItem ("Preferences");
    //adds event handler to the item
    preferencesItem.addActionListener (new MenuItemHandler(this));
    //set accelerator keys to Alt and P
    preferencesItem.setAccelerator ((KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK)));
    //sets tool tip hints
    preferencesItem.setToolTipText ("Edit and save browser preferences");
    //adds the preferencesItem to the Edit menu
    editMenu.add (preferencesItem);
    
    //adds the Edit menu to the menu bar
    menuBar.add (editMenu);
    
    //creates a menu named View
    JMenu viewMenu = new JMenu ("View");
    //creates a menu item named Clear History
    JMenuItem clearHistoryItem = new JMenuItem ("Clear History");
    //adds event handler to the item
    clearHistoryItem.addActionListener (new MenuItemHandler() );
    //set accelerator keys to Alt and backspace
    clearHistoryItem.setAccelerator ((KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, ActionEvent.ALT_MASK)));
    //sets tool tip hints
    clearHistoryItem.setToolTipText ("Deletes all items in the history menu");
    //adds the clearHistoryItem to the history submenu
    historyMenu.add (clearHistoryItem);
    //adds a separator after the clearHistoryItem
    historyMenu.addSeparator ();
    //adds the historyMenu submenu inside the View menu
    viewMenu.add (historyMenu);
    
    //adds the View menu to the menu bar
    menuBar.add (viewMenu);
    
    //creates a menu named Help
    JMenu helpMenu = new JMenu ("Help");
    //creates a menu item named About and with the specified image source
    //if the image file does not exist, will not display image
    JMenuItem aboutItem = new JMenuItem ("About", new ImageIcon ("KKLogo.JPG"));
    //adds event handler to the item
    aboutItem.addActionListener (new MenuItemHandler() );
    //set accelerator keys to Alt and A
    aboutItem.setAccelerator ((KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK)));
    //sets tool tip hints
    aboutItem.setToolTipText ("About KingKong WaterDemon Browser");
    //adds the item to the Help menu
    helpMenu.add(aboutItem);
    
    //adds the Help menu to the menu bar
    menuBar.add (helpMenu);
    
    //returns the finished menu bar
    return menuBar;
  }
  
  /**
   * This method will delete the currently selected tab in the tab pane.
   * If there is only one tab inside the tab pane, this method will reset the 
   * tab to a new, blank page but will not delete that tab.  If there is only
   * one tab and the tab already contains a blank page, then the program will exit.
   * 
   */
  private void deleteTab ()
  {
    //if there are more than one tab
    if (totalTabNum > 0)
    {
      //removes the currently selected tab inside the webPageTabPane
      webPageTabPane.removeTabAt (webPageTabPane.getSelectedIndex() );
      //decrease the total number of tabs by 1
      totalTabNum --;
    }
    //if there is only one tab
    else
    {
      //if the currentWebPage is not displaying any URL website
      if (currentWebPage.getCurrentPage().equals( "New Page"))
      {
        //calls the exitProgram method to exit the program
        exitProgram();
      }
      //else if the currentWebPage is displaying a website
      else
      {
        //creates a new WebPage object and assigns it to currentWebPage
        currentWebPage = new WebPage ();
        //sets the component of the first tab to the new WebPage object
        webPageTabPane.setComponentAt (0,currentWebPage);
      }
    }
    //calls the manageDisplays method to make changes to the GUI
    manageDisplays();
    //calls the manageButtons method to make adjustments to the forward and back buttons
    manageButtons();
  }
  
  /**
   * This method displays a small message about the program using
   * a JOptionPane.
   */
  private void displayAbout ()
  {
    //creates a message to be displayed
    String message = String.format ("%s\n%s\n%s", "Name of Program: KingKong Browser",
                                    "Name of Creator: Kevin Kong", "Date Created: June 2, 2011");
    //displays the message using JOptionPane's message dialog
    //includes a picture, but if picture source does not exist then picture will not show
    JOptionPane.showMessageDialog (this, message, "About Browser", JOptionPane.PLAIN_MESSAGE, new ImageIcon ("KKLogo.JPG"));
  }
  
  
  /**
   * This method takes in a String object and checks to see if it is in a valid
   * format to be a URL. If the parameter is already in valid format, the method will
   * return the original parameter, or else return a properly formated URL from the
   * parameter.
   * 
   * @param location The desired URL entered by the user.
   * @return A properly formated URL, with http:// at the beginning.
   */
  private String editURL (String location)
  {
    //if the location has less than VALIDURLTITLE letters (not enough to have http://
    if (location.length() < VALIDURLTITLE)
    {
      //adds "http://" to the front of the link
      location = "http://" + location;
    }
    //else if the location is more than VALIDURLTITLE letters
    else
    {
      //if the first VALIDURLTITLE letters of the link is not equal to http://
      if (!location.substring(0,VALIDURLTITLE).toUpperCase().equals("HTTP://") )
      {
        //adds "http://" to the front of the link
        location = "http://" + location;
      }
    }
    //returns the formated link
    return location;
  }
  
  /**
   * This method will safely exit the program.
   * If there are more than one tabs open, this method will prompt the user
   * if they wish to exit and then handle the response accordingly.
   */
  private void exitProgram ()
  {
    //if the value of totalTabNum is creater than 0
    //ie there are more than 1 tab open
    if (totalTabNum > 0)
    {
      //creates option buttons to be added to the message
      String [] buttons = {"Yes, Exit", "No, Continue"};
      //defaults the initial selection to the first button
      int initialSelection = 0;
      //formats message
      String message = String.format ("%s\n%s", "There are two or more tabs open.", "Do you wish to exit?");
      //calls the JOptionPane to show option dialog with the message and buttons
      //will display a question mark and set initial selection to the first button
      //captures the user's response in 'response'
      int response = JOptionPane.showOptionDialog (this, message, "Exit", JOptionPane.YES_NO_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE, null, buttons, initialSelection);
      //if the user pressed the first button
      if (response == initialSelection)
      {
        //exits the program
        System.exit(TERMINATEPROGRAM );
      }
      //if the user pressed the second button, the program will do nothing
    }
    //else if there is only one tab open
    else
    {
      //exits the program
      System.exit(TERMINATEPROGRAM );
    }
  }
  
  /**
   * This method takes in a String URL parameter and will
   * display the URL inside the currently selected tab.
   * 
   * @param location The URL of the desired website.
   */
  private void getThePage (String location)
  {
    //calls the editURL method to check and validate the URL provided
    //modifies the URL if the user didn't type "http://"
    location = editURL (location);
    //calls the manageMasterHistory method to add the URL to the master history
    manageMasterHistory(location);
    //set the currentWebPage object to display the contents of the website of the URL
    currentWebPage.setThePage (location);
    
    //calls the manageDisplays method to make changes to the GUI
    manageDisplays();
    //calls the manageButtons method to make adjustments to the forward and back buttons
    manageButtons();
  } 
  
  /**
   * This method takes no parameters and will disable/enable the forward and back
   * buttons depending on current updated situations.
   *
   */
  private void manageButtons ()
  {
    //if the currentWebPage object does not have a previous page in its history
    if (currentWebPage.hasPreviousPage() == false)
    {
      //disable the back button
      backButton.setEnabled (false);
    }
    //else if the currentWebPage object has a previous page in its history
    else
    {
      //enables the back button
      backButton.setEnabled (true);
    }
    
    //if the currentWebPage object does not have a next page in its history
    if (currentWebPage.hasNextPage() == false)
    {
      //disable the forward button
      forwardButton.setEnabled(false);
    }
    //else if the currentWebPage object has a next page in its history
    else
    {
      //enables the forward button
      forwardButton.setEnabled(true);
    }
  }
  
  /**
   * This method will update the GUI based on current information.
   * This method will appropriately set the text inside the text field and tab title,
   * as well as enable or disable the history menu depending on the number of items inside.
   * This method also will manage the home button as well as set/change the look and
   * feel of the program.
   * 
   */
  private void manageDisplays ()
  {
    //set the text of the text field to display the URL of the currentWebPage
    addressEnterField.setText (currentWebPage.getCurrentPage() );
    //set the title of the currently selected tab to the URL of the currentWebPage
    //calls the restrictTextSize method to restrict the length of the title of the tab
    //this sets a maximized size for the tabs, but does not change the actual links
    webPageTabPane.setTitleAt (webPageTabPane.getSelectedIndex(), restrictTextSize(0,currentWebPage.getCurrentPage()) );
    
    //if the history menu has fewer or equal number of items than the minimum of 2 items
    //ie only contains the clear history button and the separator
    if (historyMenu.getItemCount() <= MINIMUMHISTORYSIZE)
    {
      //disable the history menu
      historyMenu.setEnabled(false);
    }
    //else if the history menu contains more items than the minimum
    else
    {
      //enable the history menu
      historyMenu.setEnabled(true);
      //if the history size is not zero, not unlimited
      if (historySize != MAXIMUMHISTORYSIZE)
      {
        //while loop
        //while there are more items in the history menu than the desirable size
        while (historySize < historyMenu.getItemCount()-MINIMUMHISTORYSIZE)
        {
          //removes the item at the third position of the menu
          //only keep the most recent histories, delete the last
          historyMenu.remove(OLDESTHISTORY);
        }
      }
    }
    
    //if there are no set home page
    if (homePage == null)
    {
      //disables the home page button
      homeButton.setEnabled(false);
    }
    //else if the user did specify a home page
    else
    {
      //enables te home page button
      homeButton.setEnabled(true);
    }
    
    //if the pLAF should be changed
    //ie user just edited the preferences
    if (changePLAF)
    {
      //try and catch statement to set the look and feel
      try
      {
        //if the pLAF is Metal
        if (pLAF.equals ("Metal"))
        {
          //sets the look and feel to the MetalLookAndFeel
          UIManager.setLookAndFeel ("javax.swing.plaf.metal.MetalLookAndFeel");
        }
        //if the pLAF is Motif
        else if (pLAF.equals ("Nimbus"))
        {
          //sets the look and feel to the MotifLookAndFeel
          UIManager.setLookAndFeel ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        //if the pLAF is System
        else if (pLAF.equals ("System"))
        {
          //sets the look and feel to the current system look and feel
          UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());
        }
        //if the pLAF is Windows
        else if (pLAF.equals ("Windows"))
        {
          //sets the look and feel to the WindowsLookAndFeel
          UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        //updates the window to change to the new look and feel
        SwingUtilities.updateComponentTreeUI (this);
        //set changePLAF to false so when this method is called again
        //there won't be an error created
        changePLAF = false;
      }
      //catches an UnsupportedLookAndFeelException
      catch (javax.swing.UnsupportedLookAndFeelException unsupportedLookAndFeelException) 
      {
        String message = pLAF + " look and feel is not supported";
        //displays the message in a popup JOptionPane with a specific error Message
        JOptionPane.showMessageDialog (this, message, "Look and Feel Error", JOptionPane.ERROR_MESSAGE);
      }
      //catches an ClassNotFoundException
      catch (ClassNotFoundException classNotFoundException) 
      {
        String message = pLAF + " look and feel cannot be found";
        //displays the message in a popup JOptionPane with a specific error Message
        JOptionPane.showMessageDialog (this, message, "Look and Feel Error", JOptionPane.ERROR_MESSAGE);
      }
      //catches an InstantiationException
      catch (InstantiationException instantiateException) 
      {
        String message = pLAF + " look and feel cannot be instantiated";
        //displays the message in a popup JOptionPane with a specific error Message
        JOptionPane.showMessageDialog (this, message, "Look and Feel Error", JOptionPane.ERROR_MESSAGE);
      }
      //catches an IllegalAccessException
      catch (IllegalAccessException illegalAccessException) 
      {
        String message = "Does not have legal access to " + pLAF + " look and feel";
        //displays the message in a popup JOptionPane with a specific error Message
        JOptionPane.showMessageDialog (this, message, "Look and Feel Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  /**
   * This method takes in a String parameter and will add it to the master history list.
   * If the String URL is already existent, then it is disregarded.
   * 
   * @param location The URL to be added to the history.
   */
  private void manageMasterHistory (String location)
  {
    //calls the checkValidPage method to check if the URL is a valid URL
    //if it is a valid URL
    if (checkValidPage (location))
    {
      //if the URL is not existent in the master history
      if (masterHistory.searchByValue (location) == null)
      {
        //adds the URL to the end of the master history list
        masterHistory.addEnd (location);
        
        //creates a menu item with a restricted length of the URL
        JMenuItem visitedPage = new JMenuItem (restrictTextSize(MENUITEM, location));
        //adds event handler to the menu item
        visitedPage.addActionListener (new HistoryItemsHandler() );
        //sets the tool tip hint for the item to the full URL text
        visitedPage.setToolTipText (location);
        
        //if the history size is 0, or unlimited
        if (historySize == MAXIMUMHISTORYSIZE)
        {
          //adds the new menu item to the history menu
          historyMenu.add (visitedPage);
        }
        //else if the history size is a specified value
        else
        {
          //while there are more history menu items than the size desired
          while (numInHistory >= historySize)
          {
            //removes the third item inside the history menu
            //(the oldest history)
            historyMenu.remove (OLDESTHISTORY);
          }
          //adds the new menu item to the history menu
          historyMenu.add (visitedPage);
        }
      }
    }
  }
  
  /**
   * This method takes in a String parameter and an integer parameter
   * to specify what the String is for.  Based on if the String is for 
   * the title of a tab or the text of a menu item, this method will restrict
   * the size of the text passed in to a specified length, then return that text.
   * 
   * @param item Specifies what the text is for: 0 = Tabs, 1 = Menu Items.
   * @param text The text to be edited and changed to a desired length.
   */
  private String restrictTextSize (int item, String text)
  {
    //if the text is for the title of a tab
    if (item == TAB)
    {
      //if the text is shorter than the desired length for tabs
      if (text.length() <= ACCEPTEDTABTITLESIZE)
      {
        //while loop for when the text is shorter than the desired length for tabs
        while (text.length() <= ACCEPTEDTABTITLESIZE)
        {
          //adds an empty space after the text to increase its length
          text = text + " ";
        }
        //returns the text
        return text;
      }
      //else if the text is longer than the desired length for tabs
      else
      {
        //cuts the text to the length of the desired length minus 3
        //then adds three periods to the end of the text to signify that it has been shortened
        //total length will be that of the desired length
        //returns the edited text
        return text.substring (0, ACCEPTEDTABTITLESIZE-3) + "...";
      }
    }
    //else if the text is for a menu item
    else
    {
      //if the text is shorter than the desired length for menu items
      if (text.length() <= ACCEPTEDMENUITEMSIZE)
      {
        //while loop for when the text is shorter than the desired length for tabs
        while (text.length() <= ACCEPTEDMENUITEMSIZE)
        {
          //adds an empty space after the text to increase its length
          text = text + " ";
        }
        //returns the text
        return text;
      }
      else
      {
        //cuts the text to the length of the desired length minus 3
        //then adds three periods to the end of the text to signify that it has been shortened
        //total length will be that of the desired length
        //returns the edited text
        return text.substring (0, ACCEPTEDMENUITEMSIZE-3) + "...";
      }
    }
  }
  
  /**
   * This method loads the user saved home page into the current selected tab.
   */
  private void setHomePage ()
  {
    //if the user had actually edited and saved a home page
    if (homePage != null)
    {
      //calls the getThePage method to set the current page to the home page
      getThePage (homePage);
    }
  }
  
  /**
   * This method will retrieve the preferences from the preferencesWindow,
   * then will call the manageDisplays method to update the GUI.
   */
  private void setPreferences ()
  {
    //calls the getHomePage method of preferenceWindow and assigns the value to homePage
    homePage = preferenceWindow.getHomePage ();
    //calls the getHistorySize method of preferenceWindow and assigns the value to historySize
    historySize = preferenceWindow.getHistorySize ();
    //calls the getPLAF method of preferenceWindow and assigns the value to pLAF
    pLAF = preferenceWindow.getPLAF ();
    //calls the manageDisplays method to make changes to the GUI
    manageDisplays();
    //calls the manageButtons method to make adjustments to the forward and back buttons
    manageButtons ();
  }
  
  /**
   * This class is the event handler class for the back, forward, and home buttons.
   * This class also implements the ActionListener class.
   */
  private class ButtonHandler implements ActionListener
  {
    //overrides the abstract method
    public void actionPerformed (ActionEvent event)
    {
      //if the event is triggered by the backButton
      if (event.getSource().equals(backButton))
      {
        //calls the setPreviousPage method in currentWebPage to set the previous web site
        currentWebPage.setPreviousPage();
      }
      //else if the event is triggered by the home button
      else if (event.getSource().equals(homeButton) )
      {
        //calls the setHomePage method to set the current tab to display the home page
        setHomePage ();
      }
      //else if the event is triggered by the forward button
      else
      {
        //calls the setNextPage button in currentWebPage to set the next web site in the history list
        currentWebPage.setNextPage();
      }
      //calls the manageDisplays method to make changes to the GUI
      manageDisplays();
      //calls the manageButtons method to make adjustments to the forward and back buttons
      manageButtons();
    }
  }
  
  /**
   * This class implements the ActionListener class and is the event handler for the Menu Items inside the history menu.
   * When each of the items is clicked, the program will display the link associated
   * with that item inside the currently opened tab.
   */
  private class HistoryItemsHandler implements ActionListener
  {
    //overrides the abstract method
    public void actionPerformed (ActionEvent event)
    {
      //castes the source into a JMenuItem and gets the tool tip text associated with that item
      //then calls the getThePage method to display the page
      //uses tool tip text because the length of the item may be cut off and restricted
      getThePage (((JMenuItem)event.getSource()).getToolTipText());
    }
  }
  
  /**
   * This class implements the HyperlinkListener class and is the event handler for the JEditorPane
   * of each WebPage object.  When a hyperlink is clicked inside the pane, the JEditorPane will
   * load that page.
   */
  private class HyperlinkHandler implements HyperlinkListener
  {
    //overrides the abstract method
    public void hyperlinkUpdate (HyperlinkEvent event)
    {
      //if one of the hyperlinks is clicked on and activated
      if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
      {
        //calls the getThePage method to display the selected hyperlink URL
        getThePage (event.getURL().toString());
      }
    }
  }
  
  /**
   * This class implements the ActionListener and is the event handler class for all menu items
   * except the history menu items.
   */
  private class MenuItemHandler implements ActionListener
  {
    //instance variable
    Component parent;
    
    //default constructor with nothing
    private MenuItemHandler ()
    {
    }
    
    //constructor passing in a parent window Component
    private MenuItemHandler (Component parentWindow)
    {
      //set parent to parentWindow
      parent = parentWindow;
    }
    
    //overrides abstract method
    public void actionPerformed (ActionEvent event)
    {
      //if it was the newTabItem that was clicked
      if (event.getActionCommand().equals("New Tab"))
      {
        //calls addTab method to add a new tab to the tab pane
        addTab();
      }
      //else if the event was triggered by the deleteTabItem
      else if (event.getActionCommand().equals ("Delete Tab"))
      {
        //calls the deleteTab method to delete the currently selected tab
        deleteTab();
      }
      //else if the evnet was triggered by the preferenceItem
      else if (event.getActionCommand().equals ("Preferences"))
      {
        //set a size for the preferenceWindow
        preferenceWindow.setSize (500,245);
        //calls the displayWindow method and pass in the parent component
        //so that the preferenceWindow will be centered to the browser
        preferenceWindow.displayWindow (parent);
      }
      //else if the event was triggered by the clearHistoryItem
      else if (event.getActionCommand().equals ("Clear History"))
      {
        //removes all items from the history menu, including the clearHistoryButton and separator
        historyMenu.removeAll ();
        
        //recreates the clearHistoryButton with event handling and adds it to the history menu
        JMenuItem clearHistoryButton = new JMenuItem ("Clear History");
        clearHistoryButton.addActionListener (this);
        //assigns masterHistory to point to a new empty DoublyLinkedList
        masterHistory = new DoublyLinkedList ();
        //adds the clearHistoryButton to the history Menu
        historyMenu.add (clearHistoryButton);
        //adds a separator to the history menu
        historyMenu.addSeparator ();
        
        //calls the manageDisplays method to make changes to the GUI
        manageDisplays();
        //calls the manageButtons method to make adjustments to the forward and back buttons
        manageButtons();
      }
      //else if the event was caused by the exitItem
      else if (event.getActionCommand().equals ("Exit Browser"))
      {
        //calls the exitProgram method to exit the program
        exitProgram ();
      }
      //else if the event was caused by the aboutItem
      else if (event.getActionCommand().equals ("About"))
      {
        //calls the displayAbout method to display the about window
        displayAbout ();
      }
    }
  }
  
  /**
   * This class implements the ChangeListener class and is the event handler for the webPageTabPane.
   */
  private class TabChangeListener implements ChangeListener
  {
    //overrides the abstract method
    public void stateChanged (ChangeEvent changeEvent) 
    {
      //changes the currentWebPage object to point to the component at the currently selected tab
      currentWebPage = (WebPage)webPageTabPane.getComponentAt (webPageTabPane.getSelectedIndex() );
      //calls the manageDisplays method to make changes to the GUI
      manageDisplays();
      //calls the manageButtons method to make adjustments to the forward and back buttons
      manageButtons();
    }
  }
  
  /**
   * This class implements the ActionListener class and is the event handler for the text field.
   * When the user presses enter, this class will display the entered URL in the current selected tab.
   */
  private class TextFieldHandler implements ActionListener 
  {
    //overrides the abstract method
    public void actionPerformed (ActionEvent event)
    {
      //if the user does not leave the text field blank
      if (!event.getActionCommand().equals(""))
      {
        //calls the getThePage method to display the URL entered by the user
        getThePage (event.getActionCommand());
      }
    }
  }
  
  /**
   * This class extends the WindowAdapter class and is the event handler for the preferencesWindow object.
   * This class will detect when the preferencesWindow is closed, and then make adjustments on the preferences accordingly.
   */
  private class WindowHandler extends WindowAdapter
  {
    public void windowDeactivated (WindowEvent e)
    {
      //calls the confirmSave method to see if the changes made in preferencesWindow should be saved into the current object
      changePLAF = preferenceWindow.confirmSave ();
      //if there should be a change in preferences
      //to avoid when error message pops up, there should be no change
      if (changePLAF)
      {
        //calls the setPreferences method to set the home page, history size, and look and feel
        setPreferences();
      }
    }
  }
}
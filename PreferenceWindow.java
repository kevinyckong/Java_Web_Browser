//import all necessary java classes
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.BorderFactory;
import javax.swing.Box;
import java.util.Scanner;
import java.io.File;
import java.io.PrintStream;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


/**
 * This class creates a window to prompt the user to set preferences
 * to the browser.  This class will read and save the user's preferences
 * into a text file, and load the text file upon next execution. The
 * preferences available to be changed by the user in this class are home page,
 * size of history to keep, and the pluggable look and feel.
 * 
 * @author Kevin Kong
 * @version June 05, 2011
 */
public class PreferenceWindow extends JFrame
{
  //creates instance variables
  private String homePage; //home page address
  private int historySize; //size of history
  private String pLAF; //the look and feel
  private String previousPLAF; //the previous look and feel before saving
  private boolean validInput; //if the user's inputs are valid
  private boolean saveConfirmed = false; //if preferences should be saved
  
  private JButton okay; //an okay button
  private JButton cancel; //a cancel button
  private JTextField addressEnterField; //to enter the address
  private JTextField numberEnterField; //to enter the history size
  private JRadioButton metalStyle; //a style of look and feel
  private JRadioButton nimbusStyle; //a style of look and feel
  private JRadioButton systemStyle; //a style of look and feel
  private JRadioButton windowsStyle; //a style of look and feel
  private JCheckBox maximumSize; //a checkbox if the user wants to display all past history
  
  private final static int VALIDURLTITLE = 7; //constant when verifying the URL
  private final static int MAXIMUMHISTORYSIZE = 30; //maximum number of items in the history for good style display
  
  /**
   * This is the constructor for the class. This constructor will first load
   * the "preferences.txt" file from before to load previous settings, then instantiate
   * all instance variables and creates a preference window with all widgets properly 
   * arranged. However, this constructor will not make the window visible.
   * 
   */
  public PreferenceWindow ()
  {
    //set the title of the window
    super ("Preferences");
    
    validInput = true;
    
    //instantiate variables
    //create a new JTextField with no preset text and 30 characters and assigns it to addressEnterField
    addressEnterField = new JTextField ("", 30);
    //create a new JTextField with no preset text and 20 characters and assigns it to numberEnterField
    numberEnterField = new JTextField ("", 5);
    //creates a JButton with the text "Okay", adds event handler to it and assigns it to okay
    okay = new JButton ("Okay");
    okay.addActionListener (new ButtonHandler() );
    //creates a JButton with the text "Cancel", adds event handler to it and assigns it to cancel
    cancel = new JButton ("Cancel");
    cancel.addActionListener (new ButtonHandler() );
    
    //creates a JLabel with text
    JLabel home = new JLabel ("Home Page Address: ");
    //creates a JLabel with text
    JLabel historyList = new JLabel ("History List Size:");
    //creates radio buttons to select styles and assign them to corresponding instance variables
    metalStyle = new JRadioButton ("Metal");
    nimbusStyle = new JRadioButton ("Nimbus");
    systemStyle = new JRadioButton ("System");
    windowsStyle = new JRadioButton ("Windows");
    //creates a JCheckBox with the text,adds event handler, and assigns it to maximumSize
    maximumSize = new JCheckBox ("Maximum Size");
    maximumSize.addItemListener (new CheckBoxHandler() );
    
    //calls the retrievePreferences method to load previous settings
    retrievePreferences ();
    
    //adds event handler to each of the buttons
    metalStyle.addItemListener (new RadioButtonHandler() );
    nimbusStyle.addItemListener (new RadioButtonHandler() );
    systemStyle.addItemListener (new RadioButtonHandler() );
    windowsStyle.addItemListener (new RadioButtonHandler() );
    
    //creates a button group to manage the buttons
    ButtonGroup styleRadioButtonGroup = new ButtonGroup ();
    //adds all the radio buttons to the button group
    styleRadioButtonGroup.add (metalStyle);
    styleRadioButtonGroup.add (nimbusStyle);
    styleRadioButtonGroup.add (systemStyle);
    styleRadioButtonGroup.add (windowsStyle);
    //creates a horizontal box to arrange the buttons
    Box borderBox = Box.createHorizontalBox ();
    //adds the buttons to the box
    borderBox.add (metalStyle);
    borderBox.add (nimbusStyle);
    borderBox.add (systemStyle);
    borderBox.add (windowsStyle);
    //creates a border around the box and adds a title 
    borderBox.setBorder (BorderFactory.createTitledBorder ("Display"));
    
    //creates JPanels to hold each set of componenets and to add to various places on the window
    JPanel top = new JPanel (); //panel to be added to the top
    JPanel topCenter = new JPanel (); //panel to be added under the top panel
    JPanel lowerCenter = new JPanel (); //panel to be added above the bottom panel
    JPanel bottom = new JPanel (); //panel to be added to the bottom of the window
    
    //set the layout of the top panel to flowlayout, left justified
    top.setLayout (new FlowLayout(FlowLayout.LEFT) );
    //adds the home JLabel to the panel
    top.add (home);
    //adds the addressEnterField to the panel
    top.add (addressEnterField);
    
    //set the layout of the top center panel to flowlayout, left justified
    topCenter.setLayout (new FlowLayout(FlowLayout.LEFT) );
    //adds the history list size JLabel to the panel
    topCenter.add (historyList);
    //adds the numberEnterField to the panel
    topCenter.add (numberEnterField);
    //adds the maximumSize checkbox to the panel
    topCenter.add (maximumSize);
    
    //add the box containing the 4 radio buttons to the lower center panel
    lowerCenter.add (borderBox);
    
    //sets the layout of the bottom panel to flowlayout, bottom right justified
    bottom.setLayout (new FlowLayout (FlowLayout.TRAILING) );
    //adds the okay button to the panel
    bottom.add (okay);
    //adds the cancel button to the panel
    bottom.add (cancel);
    
    //set the layout of the window to gridbaglayout 
    setLayout (new GridBagLayout ());
    
    //creates a GridBagConstraints object to manage placement of panels
    GridBagConstraints constraints = new GridBagConstraints ();
    //modifies the constraints for each panel
    constraints.fill = GridBagConstraints.NONE; //does not resize to fit the window
    constraints.anchor = GridBagConstraints.WEST; //left aligned
    constraints.gridx = 0; //first column
    constraints.gridy = 0; //first row
    //adds the top panel into the first row
    add (top, constraints);
    constraints.gridy = 1; //changes to second row
    //adds the topCenter panel into the second row
    add (topCenter, constraints);
    constraints.gridy = 2; //changes to third row
    //adds the lowerCenter panel into the third row
    add (lowerCenter, constraints);
    constraints.gridy = 3; //changes to fourth row
    constraints.anchor = GridBagConstraints.EAST; //right justified
    //adds the bottom panel into the fourth row
    add (bottom, constraints);
    //does not allow the user to resize the window
    setResizable (false);
  }
  
  
  /**
   * This method will return a boolean value depending on if the current
   * preferences are to be saved.
   * 
   * @return True if the preferences should be saved, and false if not.
   */
  public boolean confirmSave ()
  {
    return saveConfirmed;
  }
  
  
  /**
   * This method takes in a String and checks to see if the string
   * is a valid number to be accepted into the numberEnterField as the
   * size of the history list.  This method will return a boolean 
   * value depending on the validity of the number.
   * 
   * @param text The text in the numberEnterField entered by the user.
   * @return True if the number is acceptable, false otherwise.
   */
  public boolean confirmHistorySize (String text)
  {
    //try and catch statement to convert the text to an integer
    try 
    {
      //converts the text to an integer and assigns the value to historySize
      historySize = Integer.parseInt (text);
      //if the historySize is smaller than 0 (can't be smaller than 0)
      if (historySize < 0 || historySize > MAXIMUMHISTORYSIZE)
      {
        return false;
      }
      //else if the historySize is bigger than 0 and within the maximum acceptable size (acceptable)
      else
      {
        return true;
      }
    }
    //catch an error in trying to format the text to an integer
    //if the text is not wholly made of numbers
    catch (NumberFormatException numberFormatException)
    {
      //return false
      return false;
    }
  }
  
  
  /**
   * This method takes in a String parameter and checks to see if that 
   * parameter is a valid Internet URL. This method will return a boolean
   * depending on the validity of the parameter as an URL.
   * 
   * @param link The URL entered into the addressEnterField.
   * @return True if the URL is retrievable, false if not.
   */
  public boolean confirmHomePage (String link)
  {
    //if the parameter is equal to null or empty
    if (link.equals("") || link.equals(null) || link.equals("null"))
    {
      //set the homePage to null
      homePage = null;
      //return true, because user could leave addressEnterField blank
      return true;
    }
    //if the parameter contains other values
    else
    {
      //calls the editURL method to check and alter the parameter if necessary
      link = editURL (link);
      //creates a JEditorPane object to test the URL
      JEditorPane testPane = new JEditorPane ();
      //try and catch statement to test and retrieve the URL
      try
      {
        //retrieve the URL
        testPane.setPage (link);
        //set homePage to equal to the parameter
        homePage = link;
        //return true indicating the address is valid
        return true;
      }
      //if there was an IOException error trying to set the page
      catch (IOException ioException)
      {
        //return false, indicating a faulty URL
        return false;
      }
    }
  }
  
  
  /**
   * This method will make the window disappear by setting visible to false.
   */
  public void disappear ()
  {
    setVisible (false);
  }
  
  
  /**
   * This method will make the window appear and centered onto a parent Component.
   * 
   * @param parent The parent Component window.
   */
  public void displayWindow (Component parent)
  {
    //calls setPreferences method to configure the widgets on the window 
    setPreferences ();
    //centers the current window to the parent window
    setLocationRelativeTo (parent);
    //set the window to appear
    setVisible (true);
    //sets the look and feel before the save and display of windows to the original pLAF
    previousPLAF = pLAF;
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
   * This is the accessor method for the instance variable historySize.
   * This method takes no parameters and returns the int value of variable historySize.
   *
   * @return The integer value of the instance variable historySize.
   */
  public int getHistorySize ()
  {
    return historySize;
  }
  
  
  /**
   * This is the accessor method for the instance variable homePage.
   * This method takes no parameters and returns the String value of variable homePage.
   *
   * @return The String value of the instance variable homePage.
   */
  public String getHomePage ()
  {
    return homePage;
  }
  
  
  /**
   * This is the accessor method for the instance variable pLAF.
   * This method takes no parameters and returns the String value of variable pLAF.
   *
   * @return The String value of the instance variable pLAF.
   */
  public String getPLAF ()
  {
    return pLAF;
  } 
  
  
  /**
   * This method retrieves the previous preferences settings which are saved in a filed titled
   * "preferences.txt".  If this text file is corrupt or not found, this method will set all
   * preferences options to default options.
   */
  public void retrievePreferences ()
  {
    //specifies a file object to be opened
    File inputFile = new File ("Preferences.txt");
    //if the file exists in the folder
    if (inputFile.exists() )
    {
      //try and catch statement to read the files
      try 
      {
        //create a Scanner object and pass in the file to read
        Scanner fileInput = new Scanner (inputFile);
        
        //gets the first line of the file and set it to 'page'
        String page = fileInput.nextLine ();
        //calls the confirmHomePage method to set 'page' to 'homePage'
        //if the confirmHomePage method returns false indicating the link is wrong
        if (!confirmHomePage(page) )
        {
          //purposely throws an exception to handle the corrupt file
          throw new IOException();
        }
        
        //gest the next line of the file and set it to 'size'
        String size = fileInput.nextLine ();
        //calls the confirmHistorySize method to set 'size' to 'historySize'
        //if the method returns false indicating the size is invalid
        if (!confirmHistorySize(size) )
        {
          //throws an IOException to skip to handling the corrupt file
          throw new IOException();
        }
        
        //gets the next line of the file and sets it to 'lookAndFeel'
        String lookAndFeel = fileInput.nextLine ();
        //if the text in 'lookAndFeel' does not match any of the texts of the radio buttons
        if (!lookAndFeel.equals(metalStyle.getText()) && !lookAndFeel.equals(nimbusStyle.getText()) &&!lookAndFeel.equals(systemStyle.getText()) &&
            !lookAndFeel.equals(windowsStyle.getText()) )
        {
          //throws and causes an IOException to skip to handling the corrupt file
          throw new IOException ();
        }
        //set the pLAF to 'lookAndFeel'
        pLAF = lookAndFeel;
      }
      //catch statement if there is an IOException thrown
      catch (IOException ioException)
      {
        //creates a formated error message
        String message = String.format("%s\n%s", "Cannot retrieve previous preferences settings.",
                                       "Setting preferences to default.");
        //uses the JOptionPane to display the error message in a popup window
        JOptionPane.showMessageDialog (this, message, "Preferences File Error", JOptionPane.ERROR_MESSAGE);
        //sets the default preferences
        setDefaultPreferences ();
      }
      //catch statement if there is an NoSuchElementException thrown
      catch (java.util.NoSuchElementException noSuchElementException)
      {
        //creates a formated error message
        String message = String.format("%s\n%s", "Cannot retrieve previous preferences settings.",
                                       "Setting preferences to default.");
        //uses the JOptionPane to display the error message in a popup window
        JOptionPane.showMessageDialog (this, message, "Preferences File Error", JOptionPane.ERROR_MESSAGE);
        //sets the default preferences
        setDefaultPreferences ();
      }
      //catch statement if there is an NullPointerException thrown
      catch (NullPointerException nullPointerException)
      {
        //creates a formated error message
        String message = String.format("%s\n%s", "Cannot retrieve previous preferences settings.",
                                       "Setting preferences to default.");
        //uses the JOptionPane to display the error message in a popup window
        JOptionPane.showMessageDialog (this, message, "Preferences File Error", JOptionPane.ERROR_MESSAGE);
        //sets the default preferences
        setDefaultPreferences ();
      }
    }
    //else if the file does not exist
    else
    {
      //set preferences to default values
      setDefaultPreferences ();
    }
  }
  
  
  /**
   * This method saves the current values of the instance variables homePage,
   * historySize, and pLAF into a text file named "Preferences.txt". This method
   * will overwrite existing data if the file already exists.
   */
  public void savePreferences ()
  {
    //try and catch statement for file writing
    try
    {      
      //creates a PrintSream object to write into file "Preferences.txt"
      PrintStream file = new PrintStream ("Preferences.txt");
      
      //writes into the file the value of the 3 instance variables
      file.println (homePage);
      file.println (historySize);
      file.println (pLAF);
      
      //closes the file
      file.close();
    }
    //catches an IOException
    catch (IOException ioException)
    {
      //creates a formated error message
      String message = String.format("%s\n%s", "Cannot save preferences settings.",
                                     "Preferences will be set to default for next time.");
      //displays the message in a popup JOptionPane with error Message
      JOptionPane.showMessageDialog (this, message, "Preferences File Writing Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  
  /**
   * This method will set default values to the instance variables homePage,
   * historySize, and pLAF.
   */
  private void setDefaultPreferences ()
  {
    //sets homePage to null
    homePage = null;
    //sets the historySize to the maximum size
    historySize = MAXIMUMHISTORYSIZE;
    //sets pLAF to metal for metal look and feel 
    pLAF = "Metal";
    //calls the setPreferences method to set up the default preferences
    setPreferences ();
    //saves the default preferences
    savePreferences ();
  }
  
  
  /**
   * This method sets the preferences by configuring the different widgets 
   * on the preferences window.
   */
  public void setPreferences ()
  {
    //set the addressEnterField default text to the value of homePage
    addressEnterField.setText (homePage);
    //if the historySize is equal to 0
    if (historySize == MAXIMUMHISTORYSIZE)
    {
      //the maximumSize checkbox is checked off
      maximumSize.setSelected (true);
    }
    //if the historySize is not equal to 0
    else
    {
      //sets the text of the numberEnterField as the historySize
      numberEnterField.setText (""+historySize);
    }
    
    //try and catch statement to update the look and feel of the window
    try
    {
      //if the pLAF is null, does not have a value
      if (pLAF == null)
      {
        //default to select the metalStyle radio button
        metalStyle.setSelected (true);
        //sets the look and feel to the MetalLookAndFeel
        UIManager.setLookAndFeel ("javax.swing.pLAF.metal.MetalLookAndFeel");
        //updates the window to change to the new look and feel
        SwingUtilities.updateComponentTreeUI (this);
      }
      //else if the pLAF contains a value
      else
      {
        //if the pLAF is equal to the text of the metalStyle radio button
        if (pLAF.equals(metalStyle.getText()) )
        {
          //select the metalStyle radio button
          metalStyle.setSelected(true);
          //sets the look and feel to the MetalLookAndFeel
          UIManager.setLookAndFeel ("javax.swing.plaf.metal.MetalLookAndFeel");
        }
        //else if the pLAF is equal to the text of the nimbusStyle radio button
        else if (pLAF.equals (nimbusStyle.getText()) )
        {
          //select the nimbusStyle radio button
          nimbusStyle.setSelected (true);
          //sets the look and feel to the MotifLookAndFeel
          UIManager.setLookAndFeel ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        //else if the pLAF is equal to the text of the systemStyle radio button
        else if (pLAF.equals (systemStyle.getText()) )
        {
          //select the systemStyle radio button
          systemStyle.setSelected (true);
          //sets the look and feel to the current System LookAndFeel
          UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());
        }
        //else if the pLAF is equal to the text of the windowsStyle radio button
        else if (pLAF.equals (windowsStyle.getText()) )
        {
          //select the windowsStyle radio button
          windowsStyle.setSelected (true);
          //sets the look and feel to the WindowsLookAndFeel
          UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        //updates the window to change to the new look and feel
        SwingUtilities.updateComponentTreeUI (this);
      }
    }
    //catches an UnsupportedLookAndFeelException
    catch (javax.swing.UnsupportedLookAndFeelException unsupportedLookAndFeelException) 
    {
      String message = pLAF + " look and feel is not supported";
      //displays the message in a popup JOptionPane with error Message
      JOptionPane.showMessageDialog (this, message, "Look and Feel Error", JOptionPane.ERROR_MESSAGE);
      //sets the look and feel to the default metal style
      pLAF = metalStyle.getText();
      metalStyle.setSelected (true);
    }
    //catches an ClassNotFoundException
    catch (ClassNotFoundException classNotFoundException) 
    {
      String message = pLAF + " look and feel cannot be found";
      //displays the message in a popup JOptionPane with error Message
      JOptionPane.showMessageDialog (this, message, "Look and Feel Error", JOptionPane.ERROR_MESSAGE);
      //sets the look and feel to the default metal style
      pLAF = metalStyle.getText();
      metalStyle.setSelected (true);
    }
    //catches an InstantiationException
    catch (InstantiationException instantiateException) 
    {
      String message = pLAF + " look and feel cannot be instantiated";
      //displays the message in a popup JOptionPane with error Message
      JOptionPane.showMessageDialog (this, message, "Look and Feel Error", JOptionPane.ERROR_MESSAGE);
      //sets the look and feel to the default metal style
      pLAF = metalStyle.getText();
      metalStyle.setSelected (true);
    }
    //catches an IllegalAccessException
    catch (IllegalAccessException illegalAccessException) 
    {
      String message = "Does not have legal access to " + pLAF + " look and feel";
      //displays the message in a popup JOptionPane with error Message
      JOptionPane.showMessageDialog (this, message, "Look and Feel Error", JOptionPane.ERROR_MESSAGE);
      //sets the look and feel to the default metal style
      pLAF = metalStyle.getText();
      metalStyle.setSelected (true);
    }
  }
  
  
  /**
   * This is the event handler class for the JButtons in this PreferenceWindow class.
   * This implements the ActionListener class and overrides the actionPerformed 
   * method to suit the needs of the PreferenceWindow class.
   */
  public class ButtonHandler implements ActionListener
  {
    //overrides method for when the buttons are clicked
    public void actionPerformed (ActionEvent event)
    {
      //if the event is triggered by a button with text "Okay"
      if (event.getActionCommand().equals("Okay") )
      {
        //calls the confirmHomePage method with the text inside the addressEnterField
        //and assigns the boolean valueto 'validInput'
        validInput = confirmHomePage(addressEnterField.getText() );
        //if the checkbox is checked off
        if (!maximumSize.isSelected() )
        {
          //calls the confrimHistorySize method with the text inside the numberEnterField
          //and assigns the boolean evaluation of the result and the value in 'validInput'
          //into 'validInput'
          validInput = validInput && confirmHistorySize(numberEnterField.getText() );
        }
        //if one of the inputs are not valid
        if (!validInput)
        {
          //creates a formatted message to guide the user
          String message = String.format("%s\n\n%s\n%s\n\n%s", "The home page or the history size entered is not valid!!",
                                         "Note that the home page can only be set to a valid website URL,",
                                         "and that the history size has to be an integer bigger than 0 but smaller than 30",
                                         "Please edit your choices and try again");
          //displays the error message with a popup JOptionPane window
          JOptionPane.showMessageDialog (null, message, "Input Error", JOptionPane.ERROR_MESSAGE );
        }
        //else if the inputs are valid
        else
        {
          //calls the savePreferences method to save the preferences to file
          savePreferences ();
          //calls the disappear method to make the window disappear
          disappear ();
        }
        //changes 'saveConfirmed' to true, indicating changes are saved
        saveConfirmed = true;
      }
      //if the event is triggered by a button not with text "Okay" (cancel button)
      else
      {
        //restores the previous look and feel of the window
        pLAF = previousPLAF;
        //sets the preferences again to clear the last look and feel
        //this step is necessary because if the last look and feel is not cleared it will mess up
        //the parent KingKongBrowser window
        setPreferences ();
        //calls the disappear method to make the window disappear
        disappear ();
        //changes 'saveConfirmed' to false, indicating changes are not to be saved
        saveConfirmed = false;
        //set the pLAF to the previous look and feel
        pLAF = previousPLAF;
      }   
    }
  }
  
  
  /**
   * This class implements the ItemListener class and is the event handler
   * for the maximumSize JCheckBox item.
   */
  public class CheckBoxHandler implements ItemListener 
  {
    //overrides the method for when the check box changes states
    public void itemStateChanged (ItemEvent event)
    {
      //if the maximumSize check box is checked off
      if (maximumSize.isSelected() )
      {
        //set historySize to the maximum size
        historySize = MAXIMUMHISTORYSIZE;
        //clears the numberEnterField text field of any text
        numberEnterField.setText ("" + MAXIMUMHISTORYSIZE);
        //disables the numberEnterField
        numberEnterField.setEditable (false);
      }
      //else if the maximumSize check box is not checked off
      else
      {
        //enables the numberEnterField
        numberEnterField.setEditable (true);
      }
    }
  }
  
  
  /**
   * This class implements the ItemListener class and is the event
   * handler for the radio buttons in the PreferenceWindow class.
   */
  private class RadioButtonHandler implements ItemListener 
  {
    //overrides the method for when the state of the button is changed
    public void itemStateChanged (ItemEvent event)
    {
      //if the metalStyle radio button is selected
      if (metalStyle.isSelected() )
      {
        //set pLAF to the text of the button
        pLAF = metalStyle.getText();
        //calls the setPreferences method to change display of the window
        setPreferences ();
      }
      //else if the nimbusStyle radio button is selected
      else if (nimbusStyle.isSelected() )
      {
        //set pLAF to the text of the button
        pLAF = nimbusStyle.getText();
        //calls the setPreferences method to change display of the window
        setPreferences ();
      }
      //else if the systemStyle radio button is selected
      else if (systemStyle.isSelected() )
      {
        //set pLAF to the text of the button
        pLAF = systemStyle.getText();
        //calls the setPreferences method to change display of the window
        setPreferences ();
      }
      //else if the windowsStyle radio button is selected
      else if (windowsStyle.isSelected() )
      {
        //set pLAF to the text of the button
        pLAF = windowsStyle.getText();
        //calls the setPreferences method to change display of the window
        setPreferences ();
      }
    }
  }
}
//import necessary classes
import javax.swing.JFrame;
import java.awt.Toolkit;

/**
 * This class creates a KingKongBrowser object, and then displays to the screen.
 * The object will first be displayed to the maximized size the screen allows
 * and will be centered on the screen.
 * 
 * @see KingKongBrowser
 */
public class KingKongBrowserTest
{
  /**
   * This is the main method of this class.
   * This method will create and instantiate a KingKongBrowser object and
   * output it to the screen.
   * 
   * @author Kevin Kong
   * @version June 05, 2011
   * @see KingKongBrowser
   */
  public static void main (String args[])
  {
    //creates a KingKongBrowser object 
    KingKongBrowser application = new KingKongBrowser ();
    //set the default close operations for the object
    application.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    //application.setExtendedState(JFrame.MAXIMIZED_BOTH);
    //creates a Toolkit object to customize the size of the browser
    Toolkit tk = Toolkit.getDefaultToolkit();
    //gets the width of the screen and assign the value to variabe width
    int width = ((int) tk.getScreenSize().getWidth());
    //gets the height of the screen and assign the value to variable height
    //height has to be decreased by 30 to account for the windows toolbar
    int height = ((int) tk.getScreenSize().getHeight()) - 30;
    //set the size of the screen to the maximized possible size
    application.setSize (width, height);
    //makes the object visible
    application.setVisible (true);
    //centers the browser object on the screen
    application.setLocationRelativeTo (null);
  }
}



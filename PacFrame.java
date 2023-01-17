//Kaan Cinar && Bogachan Arslan && Onder Soydal && Sinan Karabocuoglu
//PacFrame
//24.04.2018
/*PacFrame extends JFrame and implements KeyListener and ActionListener. PacFrame provides
 * a frame for the game while having a Timer to change variables in time, and it also checks
 * inputs taken from keyboard. In this class also game finishes or game stops.
 * The constructor of this class is enough to display everything as a JFrame.
 * Other methods are the methods od KeyListener and ActionListener.
 * From those methods, only keyPressed and actionPerformed is used.
 * keyPressed is used to get user inputs while the game is unfinished.
 * actionPerformed is used is used to update graphics and checking if game is finished.*/

//imports
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class PacFrame extends JFrame implements KeyListener,ActionListener{ // by Kaan Cinar
  
  PacComp kcomp; //the JComponent class that shows the graphics
  private static Timer t; //the timer class to update the graphics and check important cases in the game shuch as dying or winning
  boolean stopped = true; //this boolean shows if the game is stopped
  
  //during initialization frame is set, keylistener and action listener are set
  public PacFrame() {
    setLayout(new BorderLayout());
    kcomp = new PacComp();
    getContentPane().add(kcomp, BorderLayout.CENTER); //game is in center
    addKeyListener(this); //the keylistener is added
    ActionListener listener = this; //actioanlistener is initialized
    final int DELAY = 20; 
    t = new Timer(DELAY, listener);
    //frame settings
    setTitle("PAC-MAN");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(994,738));
    setResizable(false);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
  
  public void keyPressed(KeyEvent e) {
    //inputs have a use while game is not finished
    if(!(kcomp.gameOver||kcomp.win)){
      //if game is not stopped and key code is space game stops
      if (!stopped&&e.getKeyCode() == KeyEvent.VK_SPACE){
        kcomp.stopped = true;
        stopped = true;
        t.stop(); //timer is stopped to stop the game
        kcomp.reDraw();
      }
      //if game is stopped and keycode is space game continues
      else if (stopped&&e.getKeyCode() == KeyEvent.VK_SPACE){ 
        kcomp.stopped = false;
        stopped = false;
        t.start(); //timer is started to continue the game
        kcomp.reDraw();
      }
      kcomp.pac.getInput(e.getKeyCode()); //takes input for pacman
    }
  }
  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e){}
  public void actionPerformed(ActionEvent event){
    if(kcomp.scores.score>=12150) { //if scores excede 12150 game is won
      kcomp.win =true; //this is set true to change the graphics to show the user that he won
      t.stop();
    }
    if(kcomp.scores.lives<=0) { //if there is no life game is lost
      kcomp.gameOver =true; //this is set true to change the graphics to show the user that he lost
      t.stop();
    }
    kcomp.reDraw(); //method to update variables and graphics
  }
}
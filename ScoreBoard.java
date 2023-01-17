//Kaan Cinar && Bogachan Arslan && Onder Soydal && Sinan Karabocuoglu
//ScoreBoard by Sinan Karabocuoglu
//24.04.2018
import javax.swing.*;
import java.awt.*;
public class ScoreBoard{
  //attitudes
  public int score;//score of the player
  public int lives;//lives of the player
  
  //constructer
  public ScoreBoard(){
    score = 0;//score is initially 0
    lives = 3;//a player has 3 lives
  }
  
  public void draw(Graphics g){//draw method
    Graphics2D g2 = (Graphics2D) g;
    float stroke = 12;// a float value
    BasicStroke strokeline = new BasicStroke(stroke);//a stroke line
    Rectangle myRect = new Rectangle(5,5,983,24);//creates a rectangle
    g2.setColor(Color.BLUE);//sets the color blue
    g2.setStroke(strokeline);//enclose it with blue lines
    g2.draw(myRect);//draws the rectangle
    g2.setColor(Color.BLACK);//sets the color to black
    g2.fill(myRect);//fills the rectangle with black
    g2.setColor(Color.WHITE);//sets the color to white
    Font trb = new Font("Courier", Font.PLAIN, 20);//sets a different font
    g2.setFont(trb);//sets the font
    g2.drawString("Score: "+score, 25,25);//writes the score
    g2.drawString("THE PACMAN GAME", 407,25);//writes the name of the game
    g2.drawString("Lives: "+lives, 800,25);//writes the amount of lives
  }
  
  public void died()
  {
    lives--;
  }//when died, player will lose one life
  
  public void getPoint(){
    score += 50;
  }//when a food is eaten, the player will earn 50 points
}

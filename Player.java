//Kaan Cinar && Bogachan Arslan && Onder Soydal && Sinan Karabocuoglu
//Player
//24.04.2018
/*This class has variables and methods of the pacman, the player character. It has move() method
 * to handle the changing variables that are dependent or independent of user input. It takes user 
 * input via getInput() to change directions of pacman. The draw() function of this class draws 
 * the pacman in component class.*/

//imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Player { // by Kaan Cinar
  //attitudes
  int xpos; 
  int ypos;
  int diameter;
  int angle = 30; //used for mouth movement
  int theta = 30; //used for mouth movement
  int close = 5; //used for mouth movement
  public Rectangle2D border; //these borders are used for wall detection, ghost chase, and food eating
  public Rectangle2D upBor;
  public Rectangle2D downBor;
  public Rectangle2D leftBor;
  public Rectangle2D rightBor;
  int xvel = 0; //velocity for movement
  int yvel = 0;
  char direction = 'E'; //shows direction
  public boolean up,down,right,left; //booleans to move the pacman
  public boolean u,d,r,l = true; //these booleans check if directions are empty
  
//constructer, initializes the variables
  public Player(int x, int y, int size) {
    xpos = x;
    ypos = y;
    diameter = size;
    border = new Rectangle2D.Double(x, y, diameter, diameter); //these borders are used for wall detection, ghost chase, and food eating
    upBor = new Rectangle2D.Double(x-1, y-13, diameter+2, 12); //they are dependent of the position of the pacman
    downBor = new Rectangle2D.Double(x-1, y+diameter+1, diameter+2, 12);
    leftBor = new Rectangle2D.Double(x-13, y-1, 12, diameter+2);
    rightBor = new Rectangle2D.Double(x+diameter+1, y-1, 12, diameter+2);
  }
  
  //draws pacman
  public void draw(Graphics g){
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.YELLOW);
    /*g2.draw(upBor); //this draw methods that are commented are used to show the borders
     g2.draw(downBor);
     g2.draw(leftBor);
     g2.draw(rightBor);*/
    //the arc is the shape of pacman
    g2.fillArc((int) border.getBounds2D().getX(), (int) border.getBounds2D().getY(), diameter, diameter,angle-theta,300+2*theta);
  }
  
  //gets x coordinate of pacman
  public int getX(){
    return (int)border.getBounds2D().getX(); //uses the shape border to get the x coordinate
  }
  
  //gets y coordinate of pacman
  public int getY(){
    return (int)border.getBounds2D().getY(); //uses the shape border to get the y coordinate
  }
  
  //gets the char that define the direction that the pacman is header
  public char getDirection(){
    return direction;
  }
  
  //used to get input for directions
  public void getInput(int i){
    up = false; //in each input, every boolean set to false
    down = false;
    right = false;
    left = false;
    if(i == 38){ //if input defines a direction, then that direction is set as true
      up = true;
    }
    if(i == 39){
      right = true;
    }
    if(i == 37){
      left = true;
    }
    if(i == 40){
      down = true;
    }
  }
  
  //returns the Rectangle2D that has the dimentions of the pacman
  public Rectangle2D getBorderOfPac(){
    return border;
  }
  
  //changes variables
  public void move(){
    //if in left portal, teleports pacman to right portal
    if((int) (border.getBounds2D().getX()+border.getBounds2D().getWidth())<0) 
    {
      //every border that is dependent on the position of pacman is set
      leftBor.setRect(988-13-3, (int) leftBor.getBounds2D().getY()+yvel, leftBor.getBounds2D().getWidth(), leftBor.getBounds2D().getHeight());
      rightBor.setRect(988+diameter+1-3, (int) rightBor.getBounds2D().getY()+yvel, rightBor.getBounds2D().getWidth(), rightBor.getBounds2D().getHeight());
      upBor.setRect(987-3, (int) upBor.getBounds2D().getY()+yvel, upBor.getBounds2D().getWidth(), upBor.getBounds2D().getHeight());
      downBor.setRect(987-3, (int) downBor.getBounds2D().getY()+yvel, downBor.getBounds2D().getWidth(), downBor.getBounds2D().getHeight());
      border.setRect(988-3, (int) border.getBounds2D().getY()+yvel, diameter, diameter);
    }
    //if in right portal, teleports pacman to left portal
    if((int) (border.getBounds2D().getX())>992) 
    {
      //every border that is dependent on the position of pacman is set
      leftBor.setRect(0-13+3-border.getBounds2D().getWidth(), (int) leftBor.getBounds2D().getY()+yvel, leftBor.getBounds2D().getWidth(), leftBor.getBounds2D().getHeight());
      rightBor.setRect(0+diameter+1+3-border.getBounds2D().getWidth(), (int) rightBor.getBounds2D().getY()+yvel, rightBor.getBounds2D().getWidth(), rightBor.getBounds2D().getHeight());
      upBor.setRect(-1+3-border.getBounds2D().getWidth(), (int) upBor.getBounds2D().getY()+yvel, upBor.getBounds2D().getWidth(), upBor.getBounds2D().getHeight());
      downBor.setRect(-1+3-border.getBounds2D().getWidth(), (int) downBor.getBounds2D().getY()+yvel, downBor.getBounds2D().getWidth(), downBor.getBounds2D().getHeight());
      border.setRect(0+3-border.getBounds2D().getWidth(), (int) border.getBounds2D().getY()+yvel, diameter, diameter);
    }
    //sets velocity variables to zero
    xvel = 0;
    yvel = 0;
    if(theta<0||theta>30) close*=-1; //changes mouth movement
    theta-=close; //changes mouth movement
    //following if loops are used for movement in different directions
    if((right||direction=='E')&&r) { //works if user clicked right key or the direction is defined as east and right of pacman is empty
      //this two if statement aligns pacman in its place if it is higher or lower than where it should be by checking up and down borders
      if(!u&&d) yvel=4; 
      if(!d&&u) yvel=-4;
      direction = 'E';
      xvel=4;
      angle = 30; //angle is where the mouth of pacman is
    }
    if((left||direction =='W')&&l) { //works if user clicked left key or the direction is defined as west and left of pacman is empty
      //this two if statement aligns pacman in its place if it is higher or lower than where it should be by checking up and down borders
      if(!u&&d) yvel=4;
      if(!d&&u) yvel=-4;
      direction = 'W';
      xvel=-4;
      angle = 210; //angle is where the mouth of pacman is
    }   
    if((up||direction=='N')&&u) { //works if user clicked up key or the direction is defined as north and up of pacman is empty
      //this two if statement aligns pacman in its place if it is left or right than where it should be by checking left and right borders
      if(!l&&r) xvel=4;
      if(!r&&l) xvel=-4;
      direction = 'N';
      yvel=-4;
      angle = 120; //angle is where the mouth of pacman is
    }
    if((down||direction=='S')&&d) { //works if user clicked down key or the direction is defined as south and down of pacman is empty
      //this two if statement aligns pacman in its place if it is left or right than where it should be by checking left and right borders
      if(!l&&r) xvel=4;
      if(!r&&l) xvel=-4;
      direction = 'S';
      yvel=4;
      angle = 300; //angle is where the mouth of pacman is
    }
    //in the Rectsangle2D variables that store the positions are set to move the position of pacman according the changing variables
    leftBor.setRect((int) leftBor.getBounds2D().getX()+xvel, (int) leftBor.getBounds2D().getY()+yvel, leftBor.getBounds2D().getWidth(), leftBor.getBounds2D().getHeight());
    rightBor.setRect((int) rightBor.getBounds2D().getX()+xvel, (int) rightBor.getBounds2D().getY()+yvel, rightBor.getBounds2D().getWidth(), rightBor.getBounds2D().getHeight());
    upBor.setRect((int) upBor.getBounds2D().getX()+xvel, (int) upBor.getBounds2D().getY()+yvel, upBor.getBounds2D().getWidth(), upBor.getBounds2D().getHeight());
    downBor.setRect((int) downBor.getBounds2D().getX()+xvel, (int) downBor.getBounds2D().getY()+yvel, downBor.getBounds2D().getWidth(), downBor.getBounds2D().getHeight());
    border.setRect((int) border.getBounds2D().getX()+xvel, (int) border.getBounds2D().getY()+yvel, diameter, diameter);
  }
}
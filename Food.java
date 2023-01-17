//Kaan Cinar && Bogachan Arslan && Onder Soydal && Sinan Karabocuoglu
//Food by Sinan Karabocuoglu
//24.04.2018
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Food{
    //attitudes
    public int xpos;//x position
    public int ypos;//y position
    public Rectangle2D bor;// the rectangle that will include the food
    public boolean eaten;// boolean that will show if the food was eaten or not
    public boolean powerUp;// boolean that will show if the food has special features, will be initialized when an object of this class is initialized
    //constructer
    public Food(int x, int y){
        xpos = x;
        ypos = y;
        eaten = false;//the food isn't eaten initially
        bor = new Rectangle2D.Double(xpos+23,ypos+23+34,10,10);//creates a rectangle

    }

    //methods

    public int getX(){
        return xpos;
    }//gets the xpos

    public int getY(){
        return ypos;
    }//gets the y pos

    public Rectangle2D getBorderOfFood(){
      return bor;
    }//gets the border of the rectangle in order to detect an intersection
    public void draw(Graphics g){//drawing method
          Graphics2D g2 = (Graphics2D) g;
          if(!eaten) {//draws the food if it isn't eaten
            g2.setColor(Color.YELLOW);//sets the color to yellow
            g2.fillOval((int)bor.getBounds2D().getX(),(int)bor.getBounds2D().getY(),(int)bor.getBounds2D().getWidth(),(int)bor.getBounds2D().getHeight());//draws an oval inside the rectangle
          }
    }
    
    public void setPowerUp(){//if the food is chosen to be a special food, the oval will be larger and the boolean powerUp will be true
      powerUp = true;
      bor = new Rectangle2D.Double(xpos+18,ypos+18+34,20,20);
    }
}
//Bogachan Arslan, Kaan Cinar, Onder Soydal, Sinan Karabocuoglu
//Pacman Midterm
//18.05.18


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Ghost {
    protected int xPos;
    protected int yPos;
    private int defaultX;
    private int defaultY;
    protected int SIZE;
    private int wallWidth=0;
    protected Color COLOR;
    public Shape boundary; //The shape surrounding ghost to check for collisions
    private AffineTransform af;
    private Random r=new Random();
    private int velocity=1;//default speed
    private int lengthMoved=0;
    private boolean moving=false;
    private Shape[][] maze; //The array that holds the walls
    public char direction='N';
    private boolean directionManuallySet=false;
    public int movementMode=0; // 0-->Scatter, 1-->Chase, 2-->Escape
    protected int timeToScatter;
    private final int timeToChase=15;
    private final int timeToEscape=15;
    private boolean oppositeDirection=false;
    private int secondsCounter=0; //To keep track of time in seconds

    public Ghost(int initialX,int initialY,int vel,int squareSize,int width, Shape[][] grid){
        maze=grid;
        xPos=initialX;
        defaultX=initialX;
        defaultY=initialY;
        yPos=initialY;
        velocity=vel;
        SIZE=squareSize;
        wallWidth=width;
        af=AffineTransform.getTranslateInstance(0,0);
        boundary=new Rectangle2D.Double(initialX-SIZE/2,initialY-SIZE/2,SIZE,SIZE);

        //The timer that will keep track of time to switch between modes
        Timer timer=new Timer();
        TimerTask chronometer=new TimerTask(){
            @Override
            public void run() {
                secondsCounter++;
            }
        };
        timer.schedule(chronometer,0,1000);
    }

    public void drawGhost(Graphics2D g2,int pacmanX,int pacmanY,char pacmanDirection){
        g2.setStroke(new BasicStroke(0));
        g2.setColor((movementMode==1)?Color.RED:Color.GREEN);
        //g2.draw(boundary.getBounds());
        g2.setColor(COLOR);

        //To switch between modes if their relative times are up
        switch (movementMode){
            case 0: //If time to scatter is up, switch to chase mode, reset timer
                if(secondsCounter>=timeToScatter){ movementMode=1; secondsCounter=0; }
                break;
            case 1: //If time to chase is up, switch to scatter mode
                if(secondsCounter>=timeToChase){ movementMode=0; secondsCounter=0; }
                break;
            case 2: //If time to escape is up, switch to chase mode
                g2.setColor(Color.BLUE);
                if(secondsCounter==11 || secondsCounter==13) g2.setColor(COLOR); //Revert to original colors in the last few seconds of escape mode to indicate it is ending
                if(secondsCounter>=timeToEscape){ movementMode=1; secondsCounter=0; }
        }

        //The if statement checks if the ghost is done with shifting a square or not
        //The ghosts only decide for the next place to go when they have stopped in the middle of a square
        if(moving) { //If it is in the middle of a movement
            for(int i=0;i<velocity;i++){ //Do these velocity times (Higher velocity = more consecutive movements)
                boundary = af.createTransformedShape(boundary); //Translate boundary
                lengthMoved++;//keep track of how many units the ghost moved
                xPos=(int)boundary.getBounds().getX()+SIZE/2; //shift x&y
                yPos=(int)boundary.getBounds().getY()+SIZE/2;
                if(lengthMoved==(/*SIZE+wallWidth*/52)) { moving=false; break;} //If the ghost moved the amount of distance from one square's center to another, the movement is complete
            }
        } else { //If not moving
            lengthMoved=0; //reset the movement mount
            af=AffineTransform.getTranslateInstance(0,0); //reset translation instructions
            initiateMovement(pacmanX,pacmanY,pacmanDirection); //then begin another movement
        }

        //Drawing of the ghost
        double scaleFactor=SIZE*3/8;
        //For circular head and horizontal rectangular body
        g2.fillArc((int)(xPos-scaleFactor),(int) (yPos-scaleFactor),(int)(2*scaleFactor),(int)(2*scaleFactor),0,180);
        g2.fillRect((int)(xPos-scaleFactor),yPos,(int)(2*scaleFactor),(int)(scaleFactor/2));

        //For triangular legs
        int[] TX={(int)(xPos-scaleFactor),(int)(xPos-scaleFactor),(int)(xPos-scaleFactor),(int)(xPos-scaleFactor/2),xPos,(int)(xPos+scaleFactor/2),(int)(xPos+scaleFactor),(int)(xPos+scaleFactor)};
        int[] TY={(int)(yPos+scaleFactor/2),(int)(yPos+scaleFactor),(int)(yPos+scaleFactor),(int)(yPos+scaleFactor/2),(int)(yPos+scaleFactor),(int)(yPos+scaleFactor/2),(int)(yPos+scaleFactor),(int)(yPos+scaleFactor/2)};
        g2.fillPolygon(TX,TY,8);

        //For Eyes
        g2.setColor(Color.WHITE);
        g2.fillOval((int)(xPos-2*scaleFactor/3),(int)(yPos-2*scaleFactor/3),(int)(2*scaleFactor/3),(int)(2*scaleFactor/3));
        g2.fillOval((xPos),(int)(yPos-2*scaleFactor/3),(int)(2*scaleFactor/3),(int)(2*scaleFactor/3));
        g2.setColor(Color.BLACK);
        //Dynamically determine eyedot locations to indicate direction of movement
        g2.fillOval(eyePositionX(direction)[0],eyePositionY(direction),(int)(scaleFactor/3),(int)(scaleFactor/3));
        g2.fillOval(eyePositionX(direction)[1],eyePositionY(direction),(int)(scaleFactor/3),(int)(scaleFactor/3));
    }

    //Determine x position of eyes according to the direction
    private int[] eyePositionX(char direction){
        double scaleFactor=SIZE*3/8;
        int[] eyesX=new int[2];
        switch(direction){
            case 'S':
                eyesX[0]=(int)(xPos-scaleFactor/2);
                eyesX[1]=(int)(xPos+scaleFactor/6);
                break;
            case 'N':
                eyesX[0]=(int)(xPos-scaleFactor/2);
                eyesX[1]=(int)(xPos+scaleFactor/6);
                break;
            case 'E':
                eyesX[0]=(int)(xPos-scaleFactor/3);
                eyesX[1]=(int)(xPos+scaleFactor/3);
                break;
            case 'W':
                eyesX[0]=(int)(xPos-2*scaleFactor/3);
                eyesX[1]=(xPos);
                break;
        }
        return eyesX;
    }

    //Determine y position of eyes according to the direction
    private int eyePositionY(char direction){
        double scaleFactor=SIZE*3/8;
        switch(direction){
            case 'S':
                return (int)(yPos-scaleFactor/3);
            case 'N':
                return (int)(yPos-2*scaleFactor/3);
            case 'E':
                return (int)(yPos-2*scaleFactor/3);
            case 'W':
                return (int)(yPos-2*scaleFactor/3);
        }
        return (int)(yPos-scaleFactor/3);
    }

    //This method will be overridden in each child to instruct how to chase pacman
    public abstract ArrayList<Character> determineDirectionOrder(int pacmanX, int pacmanY,char pacmanDirection);

    //This method randomly orders the 4 directions
    protected ArrayList<Character> generateRandomDirections(){
        ArrayList<Character> directions=new ArrayList<Character>();
        ArrayList<Character> possibilities=new ArrayList<Character>();
        possibilities.add('N');possibilities.add('S');possibilities.add('E');possibilities.add('W');
        for(int i=0;i<4;i++){
            int randomIndex=r.nextInt(possibilities.size());
            directions.add(possibilities.remove(randomIndex));
        }
        return directions;
    }

    //The method that starts the movement
    private void initiateMovement(int pacX,int pacY,char pacDir){
        //If the ghost isn't in chase mode then get directions randomly
        //If it is in, use the overridden direction finding instructions to determine the order
        ArrayList<Character> directions=(movementMode==1)? determineDirectionOrder(pacX,pacY,pacDir) : generateRandomDirections();
        directions=addReverseToLast(directions); //Then add the reverse of current direction to the end so it is the last to be tried so the ghost doesn't reverse direction in a path
        char selectedDirection='0';
        for(int i=0;i<directions.size();i++){ //Test the directions in order, whether if they will cause a crash with a wall
            Shape tempBoundary=boundary;
            char currentDir=directions.get(i);
            switch(currentDir){ //create temporary boundary to see if it intersects with a wall
                case 'N':
                    tempBoundary=createTranslatedShape(boundary,0,-(SIZE+wallWidth)/2);
                    break;
                case 'S':
                    tempBoundary=createTranslatedShape(boundary,0,(SIZE+wallWidth)/2);
                    break;
                case 'E':
                    tempBoundary=createTranslatedShape(boundary,(SIZE+wallWidth)/2,0);
                    break;
                case 'W':
                    tempBoundary=createTranslatedShape(boundary,-(SIZE+wallWidth)/2,0);
                    break;
            }
            if(moveSafe(tempBoundary)){ //If it is safe to move there break and choose that direction
                selectedDirection=currentDir;
                break;
            }
        }
        int xVel=0;
        int yVel=0;
        if(directionManuallySet) directionManuallySet=false; //If the direction was manually determined (via a call from another class) use that
        else direction=(oppositeDirection)? reverseDirection() : selectedDirection; //If not, check if ghost just entered escape mode. If so, reverse the previous direction. Otherwise use the recently determined new one.
        switch (direction){ //Set which values would change
            case 'N': xVel=0; yVel=-1; break;
            case 'S': xVel=0; yVel=1; break;
            case 'E': xVel=1; yVel=0;break;
            case 'W': xVel=-1; yVel=0; break;
        }
        af.translate(xVel,yVel); //create a translation instance from those values
        moving=true; //indicate that a movement may begin
    }

    //Puts the ghost in mode 3 = escape
    public void enterEscapeMode(){
        movementMode=2;
        oppositeDirection=true; //To indicate the ghost should go the opposite way in its first movement in this mode
    }

    //returns the opposite of the latest direction taken
    protected char reverseDirection(){
        oppositeDirection=false;
        switch (direction) {
            case 'N': return 'S';
            case 'S': return 'N';
            case 'E': return 'W';
            case 'W': return 'E';
        }
        return direction;
    }

    //To not reverse direction unless absolutely necessary, add the reverse of the current direction to the end of list
    //Note: For some reason removing the direction using .remove with the character as a parameter
    //raises an indexoutofbounds error so I had to manually locate index and then remove and add
    protected ArrayList<Character> addReverseToLast(ArrayList<Character> directions){
        int indexToRemove=0;
        for(int i=0;i<directions.size();i++){
            if(directions.get(i).equals(reverseDirection())) indexToRemove=i;
        }
        directions.add(directions.remove(indexToRemove));

        return directions;
    }

    //To create a temporary translated shape with given shifting values
    private Shape createTranslatedShape(Shape s,int xShift,int yShift){
        AffineTransform newTransformation=AffineTransform.getTranslateInstance(xShift, yShift);
        return newTransformation.createTransformedShape(s);
    }

    //Method to see if two parameters intersect
    private boolean intersects(Shape movable, Shape still){
        boolean intersects=false;
        if(still!=null && still.getBounds2D().intersects(movable.getBounds2D())){
            intersects=true;
        }
        return intersects;
    }

    //this method compares the new boundary of ghost with every wall and sees if they intersect
    private boolean moveSafe(Shape tempBoundary){ //TODO the safe move function doesn't check if movement shifts ghost out of the pane if no walls surround the game area
        for(Shape[] line:maze){
            for(Shape wall:line){
                if(intersects(tempBoundary,wall)) return false;
            }
        }
        return true;
    }

    //Ghost is eaten so is sent back to starting position
    public void ghostEaten(){
        boundary=new Rectangle2D.Double(defaultX-SIZE/2,defaultY-SIZE/2,SIZE,SIZE);
        xPos=defaultX;
        yPos=defaultY;
        movementMode=0;
        secondsCounter=0;
        moving=false;
    }

    //Ghost is sent to the location specified in the parameters
    public void teleport(int newX,int newY){
        boundary=new Rectangle2D.Double(newX-SIZE/2,newY-SIZE/2,SIZE,SIZE);
        xPos=newX;
        yPos=newY;
        moving=false;
    }

    //To manually set next direction to take if need be
    //This method is used to keep the ghosts from escaping the map from the empty edges to the sides of the map
    public void setDirection(char dir){
      directionManuallySet=true;
      direction=dir;
    }

    //Get methods for current position
    public int getX(){
        return xPos;
    }
    public int getY(){
        return yPos;
    }
}
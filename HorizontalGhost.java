//Kaan Cinar && Bogachan Arslan && Onder Soydal && Sinan Karabocuoglu
//HorizontalGhost
//24.04.2018
import java.awt.*;
import java.util.ArrayList;

public class HorizontalGhost extends Ghost {
    public HorizontalGhost(int initialX, int initialY, int vel, int squareSize, int width, Shape[][] grid) {
        super(initialX, initialY, vel, squareSize,width, grid);
        COLOR=Color.PINK;
        timeToScatter=6;
    }

    @Override
    public ArrayList<Character> determineDirectionOrder(int pacmanX, int pacmanY,char pacmanDirection){
        int[] direction={pacmanX-xPos,pacmanY-yPos};
        ArrayList<Character> directions=new ArrayList<Character>();
        //Compare x & y values and add the possible directions to the list accordingly
        //Priority is given to horizontal directions
        //X values are compared first
        if(direction[0]==0 && direction[1]==0){
            directions.add('W');directions.add('E');directions.add('S');directions.add('N');
        } else {
            if (direction[0] > 0) { directions.add('E');directions.add('W'); }
            else if (direction[0] < 0) { directions.add('W');directions.add('E'); }

            if (direction[1] > 0) { directions.add('S');directions.add('N'); }
            else if (direction[1] < 0) { directions.add('N');directions.add('S'); }



            if(direction[0]==0) { directions.add('E'); directions.add('W'); }
            if(direction[1]==0) { directions.add('N'); directions.add('S'); }
        }
        return directions;

    }
}

//Kaan Cinar && Bogachan Arslan && Onder Soydal && Sinan Karabocuoglu
//DistanceGhost
//24.04.2018
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DistanceGhost extends Ghost {
    private final int dangerAreaWidth=4; //The radius of the ghost's danger zone where he starts chasing pacman b

    public DistanceGhost(int initialX, int initialY, int vel, int squareSize,int width, Shape[][] grid) {
        super(initialX, initialY, vel, squareSize, width, grid);
        COLOR=Color.cyan;
        timeToScatter=4;
    }

    @Override
    public ArrayList<Character> determineDirectionOrder(int pacmanX, int pacmanY, char pacmanDirection) {
        double distanceToPacMan=Math.sqrt(Math.pow(pacmanX-xPos,2)+Math.pow(pacmanY-yPos,2));
        double searchDistance=Math.sqrt(Math.pow(dangerAreaWidth*SIZE,2)*2);
        if(distanceToPacMan<=searchDistance){ //Calculates the distance from pacman to see if pacman is in danger zone
            HashMap<Character,Double> newDistances=new HashMap<Character,Double>();
            ArrayList<Character> directions=new ArrayList<Character>();
            //if so, calculates the distances between the two if ghost were to move to a direction
            newDistances.put('S',Math.sqrt(Math.pow(pacmanX-xPos,2)+Math.pow(pacmanY-(yPos+SIZE),2)));
            newDistances.put('E',Math.sqrt(Math.pow(pacmanX-(xPos+SIZE),2)+Math.pow(pacmanY-yPos,2)));
            newDistances.put('W',Math.sqrt(Math.pow(pacmanX-(xPos-SIZE),2)+Math.pow(pacmanY-yPos,2)));
            newDistances.put('N',Math.sqrt(Math.pow(pacmanX-xPos,2)+Math.pow(pacmanY-(yPos-SIZE),2)));

            //Orders them ascendingly using helper method
            while (!newDistances.isEmpty()){
                char keyWithMinVal=findKeyWithMinValue(newDistances);
                directions.add(keyWithMinVal);
                newDistances.remove(keyWithMinVal);
            }
            return directions; //returns the order
        }
        return generateRandomDirections(); //if not, get directions randomly
    }

    //Finds the key with lowest value and returns it
    private char findKeyWithMinValue(HashMap<Character,Double> map){
        double minVal=99999999;
        char keyWithMinVal='0';
        for(char c:map.keySet()){
            if(map.get(c)<=minVal){
                minVal=map.get(c);
                keyWithMinVal=c;
            }
        }
        return keyWithMinVal;
    }
}

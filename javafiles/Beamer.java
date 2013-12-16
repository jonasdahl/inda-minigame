import java.awt.Color;

/**
 * Class that handles the beamer.
 * 
 * @author Jonas Dahl
 * @version 2013.12.13
 */
public class Beamer extends Item {
    private static final Color BEAMER_COLOR = new Color(0, 128, 128);
    private Room toRoom;
    
    /**
     * Constructor for objects of class Beamer
     * @param name the name of the beamer
     */
    public Beamer(String name) {
        super(name);
        setColor(BEAMER_COLOR);
        setUseWord("charge *NameOfBeamer* | fire *NameOfBeamer*");
    }
    
    /**
     * Charges the beamer with given room.
     * @param toRoom room to charge with
     */
    public void charge(Room toRoom) {
        this.toRoom = toRoom;
    }
    
    /**
     * Gets the destination of this beamer.
     * @return the destination room
     */
    public Room getDestination() {
        return toRoom;
    }
}
import java.awt.Color;

/**
 * Simulates a key in the game.
 * 
 * @author Jonas Dahl
 * @version 2013.12.02
 */
public class Key extends Item {
    private static final Color KEY_COLOR = new Color(255, 215, 0);
    /**
     * Constructor for objects of class Key
     * @param name the name of the Key.
     */
    public Key(String name) {
        super(name);
        setColor(KEY_COLOR);
    }
}

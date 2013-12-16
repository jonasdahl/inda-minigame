import java.awt.Color;

/**
 * Class that handles the animal.
 * 
 * @author Jonas Dahl
 * @version 2013.12.13
 */
public class Animal extends Item {
    private static final Color ANIMAL_COLOR = new Color(0, 128, 128);
    
    /**
     * Constructor for objects of class Animal
     * @param name the name of the animal
     */
    public Animal(String name) {
        super(name);
        setColor(ANIMAL_COLOR);
    }
}
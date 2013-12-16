import java.awt.Color;

/**
 * Class that handles the map.
 * 
 * @author Jonas Dahl
 * @version 2013.12.13
 */
public class WorldMap extends Item {
    private static final Color MAP_COLOR = new Color(0, 128, 128);
    
    /**
     * Constructor for objects of class Map
     * @param name the name of the map
     */
    public WorldMap(String name) {
        super(name);
        setColor(MAP_COLOR);
        setUseWord("map *NameOfMap*");
    }
}
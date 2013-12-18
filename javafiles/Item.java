import java.awt.Color;
import java.util.*;

/**
 * Handles items in the game.
 * 
 * @author Jonas Dahl
 * @version 2013.12.02
 */
public class Item {
    private String name;
    private boolean pickable;
    private Color color;
    private String useWord;
    
    /**
     * Constructor for objects of class Item.
     * @param name The name of the item.
     */
    public Item(String name) {
        this.name = name;
        this.pickable = true;
    }
    
    /**
     * Returns the name of the item.
     * @return name of item
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the color of item
     * @return color of item
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Sets the color of the item to given color.
     * @param color the color of the item
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Checks if item is pickable.
     */
    public boolean isPickable() {
        return pickable;
    }
    
    /**
     * Sets if item is pickable.
     */
    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }
    
    /**
     * Draws an item to the canvas.
     * @param canvas    the canvas to draw on
     * @param xpos      the x-position of the item
     * @param ypos      the y-position of the item
     * @param width     the width of the item
     */
    public void draw(Canvas canvas, int xpos, int ypos, int width) {
        canvas.setForegroundColor(getColor());
        canvas.fillCircle(xpos, ypos, width);
        if (this instanceof Character) {
            canvas.setForegroundColor(Color.BLACK);
            int eyeWidth = width / 4;
            int yEye = ypos + width / 4;
            int eyeMargin = 3 * width / 16;
            
            canvas.fillCircle(xpos + eyeMargin, yEye, eyeWidth);
            canvas.fillCircle(xpos + width - eyeMargin - eyeWidth, yEye, eyeWidth);
            canvas.drawLine(xpos + width / 4, ypos + 7 * width / 10, xpos + 3 * width / 4, ypos + 7 * width / 10);
        }
    }
    
    public String getUseWord() {
        return useWord;
    }
    
    public void setUseWord(String useWord) {
        this.useWord = useWord;
    }
    
    /**
     * Finds Item in given HashSet of Item:s with given name.
     * @return null if no item with given name was found, otherwise the item is returned
     */
    public static Item getItem(String name, HashSet<Item> items) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Finds "first" Item in given HashSet of Item:s with given type.
     * @return null if no item with given type was found, otherwise the item is returned
     */
    public static Item getItem(Class type, HashSet<Item> items) {
        for (Item item : items) {
            if (type.isInstance(item)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets description of item.
     */
    public String getDescription() {
        return "There is a " + name + " in here. Pick it up by typing 'pick " + name + "'";
    }
}

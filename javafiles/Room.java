import java.util.*;
import java.awt.Color;

/**
 * Class Room - a room in a game.
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes and Jonas Dahl
 * @version 2013.12.13
 */

public class Room 
{
    private Key key;                        // Key that unlocks this room, from all doors
    private String description;             // A description of the room, ie "Kitchen"
    private HashMap<String, Room> exits;    // Stores exits of this room.
    private HashSet<Item> items;            // Stores items in this room.
    private Color color;                    // The color of the room in GUI
    private int xPos;                       // The position of the rooms upper left corner
    private int yPos;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "Kitchen" or
     * "Open court yard".
     * @param description The room's description.
     */
    public Room(String description)  {
        Random random = new Random();
        this.description = description;
        this.exits = new HashMap<String, Room>();
        this.items = new HashSet<Item>();
        this.color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        this.key = null;
    }

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "Kitchen" or
     * "Open court yard". Adds a key that is necessary to open the
     * door.
     * @param description The room's description.
     * @param key         The one and only key that 
     *                    can be used to open the room.
     */
    public Room(String description, Key key) {
        this(description);
        this.key = key;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }
    
    /**
     * Add an item from this room.
     * @param item  The item to be added in room.
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Returns all items in the room.
     * @return All the items in the room as a HashSet.
     */
    public HashSet<Item> getItems() {
        return items;
    }

    /**
     * Returns the short description of the room.
     * @return The short description of the room.
     */
    public String getShortDescription() {
        return description;
    }

    /**
     * Returns a long description of the room.
     * @return A long description of this room
     */
    public String getLongDescription() {
        String returnString = "You are in: " + description + ".\n" + getExitString();
        if (!items.isEmpty())
            returnString += "\nProtip: You should take a look in here.";
        
        return returnString;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString() {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for (String exit : keys) {
            returnString += " " + exit;
        }
        returnString += ".";
        
        return returnString;
    }
    
    /**
     * Determine wheter the room is locked or not.
     * @return True if a key is needed to open this room, otherwise false.
     */
    public boolean isLocked() {
        if (key == null) 
            return false;
        return true;
    }

    
    /**
     * Determine if the room is unlocked by one of the keys given in availableItems.
     * @param  availableItems   a HashSet of items that might be able to unlock the room.
     * @return True if a key in items unlocks room, otherwise false.
     */
    public boolean unlock(HashSet<Item> availableItems) {
        for (Item thisKey : availableItems) {
            if (thisKey.equals(key))
                return true;
        }
        return false;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction, null if none.
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }

    /**
     * Gets all exits of the room.
     * @return All exits of the room as a HashMap.
     */
    public HashMap<String, Room> getExits() {
        return exits;
    }
    
    /**
     * Returns the color of the room.
     * @return The color of the room.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Sets the coordinates of the rooms upper left corner.
     * @param   xPos    the x-position of the upper left corner of room
     * @param   yPos    the y-position of the upper left corner of room
     */
    public void setCoordinates(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    /**
     * Gets the x-coordinate of the upper left corner.
     * @return The x-position of the upper left corner of room
     */
    public int getXPos() {
        return xPos;
    }
    
    /**
     * Gets the y-coordinate of the upper left corner.
     * @return The y-position of the upper left corner of room
     */
    public int getYPos() {
        return yPos;
    }
    
    /**
     * ToString method.
     */
    public String toString() {
        return "Room: " + description + ", (" + xPos + ", " + yPos + ")";
    }
}
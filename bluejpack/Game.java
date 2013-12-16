import java.awt.Color;
import java.util.*;
import java.lang.reflect.*;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.lang.Math;

/**
 *  This class is the main class of the game. 
 *  This game is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Jonas Dahl
 * @version 2013.12.13
 */

public class Game {
    // Static variables
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int ROOM_WIDTH = 100;
    private static final int ROOM_HEIGHT = 100;
    private static final int ROOM_MARGIN = 20;
    private static final int ROOM_PADDING = 5;
    private static final int OBJECT_WIDTH = 20;
    private static final int DELAY = 500;
    private static final String GAME_NAME = "Save Lil B";

    // Class variables
    private Parser parser;
    private Room currentRoom;
    private Canvas canvas;
    private HashSet<Item> items;
    private Random random;
    private boolean playing;

    /**
     * Create the game and initialise its internal map. Also starts the game. "Aka play()".
     */
    public Game() {
        canvas = new Canvas("Game", WIDTH, HEIGHT, ROOM_WIDTH, ROOM_HEIGHT, 
            ROOM_MARGIN, ROOM_PADDING, DELAY);
        parser = new Parser();
        items = new HashSet<Item>();
        random = new Random();
        createRooms();                              // Creates the rooms
        play();                                     // Starts game for player
    }

    /**
     *  Main play routine. Loops until end of play.
     */
    public void play() {  
        printWelcome();

        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.
        playing = true;
        while (playing) {
            Command command = parser.getCommand();
            processCommand(command);
        }
        Output.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        Output.println();
        Output.println("Welcome to the game " + GAME_NAME + "!");
        Output.println("This game lets you talk to nice people, and you can win!");
        Output.println("Protip: start by talking to Nedo...!");
        Output.println();
        Output.println("Type '" + CommandWord.HELP + "' if you need help (just do it!).");
        Output.println();
        Output.println(currentRoom.getLongDescription());
    }

    /**
     * Create all the rooms, items and characters and link them together.
     */
    private void createRooms() {
        Room entrance, pub, office;
        Character johan, nedo;
        Beamer beamer;
        Animal seal;
        WorldMap map;
        Key officeKey;
        HashSet<String> nedoWords, johanWords;

        // Create items
        officeKey = new Key("OfficeKey");
        beamer = new Beamer("Beamer");
        seal = new Animal("Seal");
        map = new WorldMap("Map");

        // Create rooms
        entrance = new Room("Entrance");
        pub = new Room("Pub");
        office = new Room("Office", officeKey);

        // Create characters
        nedo = new Character("Nedo");
        johan = new Character("Johan");
        nedoWords = new HashSet<String>();
        johanWords = new HashSet<String>();

        // Sets exits for the rooms
        entrance.setExit("west", pub);
        entrance.setExit("south", office);
        pub.setExit("east", entrance);
        office.setExit("north", entrance);

        // Configure characters and other items
        nedoWords.add("help");
        nedoWords.add("win");
        nedoWords.add("code");
        nedoWords.add("word");
        nedoWords.add("what");
        nedoWords.add("johan");
        johanWords.add("code");
        johanWords.add("word");
        johanWords.add("nedo");
        johanWords.add("help");
        johan.setReply(johanWords, "I know the code Nedo wants, but I want a seal to tell it.");
        johan.ifGivenSay(seal, "<< Johan: Thanks!\n   If you tell Nedo the code word: 'LILB', you will win the game.\n   You will be amazed.");
        nedo.setReply(nedoWords, "If you tell me the correct code word you will win. \n   I think Johan knows the code.");
        nedo.setWinningWord("LILB");

        pub.addItem(johan);
        pub.addItem(nedo);
        pub.addItem(beamer);
        pub.addItem(officeKey);
        office.addItem(map);
        office.addItem(seal);

        // Set up
        currentRoom = entrance;                     // Start game in entrance 
        calculateRoomCoordinates(currentRoom);
        canvas.drawRoom(currentRoom, true);
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private void processCommand(Command command) {
        CommandWord commandWord = command.getCommandWord();

        for (CommandWord cmd : CommandWord.values()) {
            if (cmd == commandWord) {
                try {
                    Method method = getClass().getMethod(commandWord.toString(), Command.class);
                    method.invoke(this, command);
                    return;
                } catch (Exception e) {
                    Output.println("I don't know what you mean...");
                } 
            }
        }
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    public void help(Command command) {
        Output.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    public void go(Command command) 
    {
        if (!command.hasSecondWords()) {
            // If there is no second word, we don't know where to go...
            Output.println("Go where? (Syntax: go west)");
            return;
        }

        String direction = command.getSecondCommands().get(0);

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            Output.println("There is no door to the " + direction + "!");
        } else {
            if (nextRoom.isLocked() && !nextRoom.unlock(items)) {
                Output.println("This door is locked! You need a key to open it.");
                return;
            } else if (nextRoom.isLocked() && nextRoom.unlock(items)) {
                Output.println("You used your key to unlock the room!");
            }
            canvas.drawRoom(currentRoom, false);
            canvas.drawRoom(nextRoom, true);
            currentRoom = nextRoom;
            Output.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * Look in the room for items.
     */
    public void fire(Command command) {
        if (!command.hasSecondWords()) {
            Output.println("Fire what? (Syntax: fire BeamerName)");
            return;
        }

        String beamerName = command.getSecondCommands().get(0);
        for (Item item : items) {
            if (item.getName().equals(beamerName)) {
                Room destinationRoom = ((Beamer) item).getDestination();
                ArrayList<String> path = getPath(currentRoom, destinationRoom);
                canvas.drawRoom(currentRoom, false);
                canvas.drawRoom(destinationRoom, true);
                currentRoom = destinationRoom;
                Output.println("You where teleported.");
                return;
            }
        }

        Output.println("You have no beamer with the name '" + beamerName + "'. (Syntax: fire BeamerName)");
    }

    /** 
     * Beams with beamer
     */
    public void charge(Command command) {
        if (!command.hasSecondWords()) {
            Output.println("Charge what? (Syntax: charge BeamerName)");
            return;
        }

        String beamerName = command.getSecondCommands().get(0);

        for (Item item : items) {
            if (item.getName().equals(beamerName)) {
                ((Beamer) item).charge(currentRoom);
                Output.println("You charged the beamer with the current room!");
                return;
            }
        }

        Output.println("You have no beamer with the name '" + beamerName + "'. (Syntax: charge BeamerName)");
    }

    /** 
     * Paints the map.
     */
    public void map(Command command) {
        if (!command.hasSecondWords()) {
            Output.println("Look in what map? (Syntax: map MapName)");
            return;
        }
        String mapName = command.getSecondCommands().get(0);

        if (items == null || items.isEmpty()) {
            Output.println("You have no map to use!");
        } else {
            for (Item item : items) {
                if (item.getName().equals(mapName)) {
                    canvas.drawAllMap(currentRoom, new ArrayList<Room>());
                    canvas.drawRoom(currentRoom, true);
                    look(new Command(CommandWord.LOOK, new ArrayList<String>()));
                    Output.println("Wow!");
                    return;
                }
            }
        }

        Output.println("Couldn't find map. (Syntax: map MapName)");
    }

    /** 
     * Gives an object to a person.
     */
    public void give(Command command) {
        if (!command.hasSecondWords() || command.getSecondCommands().size() < 2) {
            Output.println("Give what to who? (Syntax: give ItemName CharacterName)");
            return;
        }

        String itemName = command.getSecondCommands().get(0);
        String personName = command.getSecondCommands().get(1);

        Character person = null;
        for (Item item : currentRoom.getItems()) {
            if (item.getName().equals(personName)) {
                person = (Character) item;
            }
        }

        Item itemToGive = null;
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                itemToGive = (Item) item;
            }
        }

        if (itemToGive == null || person == null) {
            Output.println("There is no character with the name " + personName);
            Output.println("or there is no item with the name " + itemName);
            Output.println("(Syntax: give ItemName CharacterName)");
            return;
        }

        String response = person.isGiven(itemToGive);
        if (response != null) 
            Output.println(response);
        else
            Output.println("<< " + personName + 
                ": Thank you, but what am I going to do with a " + itemName + "?");
    }

    /** 
     * Look in the room for items.
     */
    public void look(Command command) {
        if (command.hasSecondWords()) {
            Output.println("Look what? (Syntax: look)");
            return;
        }
        
        // Erase earlier "looks"
        canvas.drawRoom(currentRoom, true);

        // Try to look in current room.
        HashSet<Item> items = currentRoom.getItems();
        int x = currentRoom.getXPos() + ROOM_PADDING;
        int y = currentRoom.getYPos() + ROOM_PADDING;

        if (items == null || items.isEmpty()) {
            Output.println("There are no items in this room!");
            return;
        }
        
        for (Item item : items) {
            if (!item.isPickable())
                Output.println(item.getName() + " is in here! Talk to him by typing 'talk " + 
                                item.getName() + "'");
            else 
                Output.println("There is a " + item.getName() + " in here. Pick it up by typing 'pick " + 
                                item.getName() + "'");

            item.draw(canvas, x, y, OBJECT_WIDTH);
            x += OBJECT_WIDTH + ROOM_PADDING;
            
            // Wrap the line of items if not space enough
            if (x > currentRoom.getXPos() + ROOM_WIDTH - ROOM_PADDING - OBJECT_WIDTH) {
                x = currentRoom.getXPos() + ROOM_PADDING;
                y += ROOM_PADDING + OBJECT_WIDTH;
            }
        }
    }

    /** 
     * Pick up item
     */
    public void pick(Command command) {
        if (!command.hasSecondWords()) {
            Output.println("What item should be picked up?");
            return;
        }

        String itemToPickUp = command.getSecondCommands().get(0);

        HashSet<Item> roomItems = currentRoom.getItems();

        for (Item item : roomItems) {
            if (item.getName().equals(itemToPickUp) && item.isPickable()) {
                items.add(item);
                roomItems.remove(item);
                Output.println("Picked up " + itemToPickUp + "!");
                
                // Erase earlier "looks"
                if (!roomItems.isEmpty())
                    Output.println("While picking up, you spotted the rest of the items in the room.");
                
                look(new Command(CommandWord.LOOK, new ArrayList<String>()));

                String useWord = item.getUseWord();
                if (useWord != null)
                    System.out.println("Use it by typing " + useWord + ".");

                return;
            }
        }

        System.out.println("Failed to pick up item '" + itemToPickUp + "'.");
    }

    /** 
     * "talk" was entered. 
     */
    public void talk(Command command) {
        boolean won = false;
        if (!command.hasSecondWords()) {
            Output.println("Talk who?");
        } else {
            for (Item person : currentRoom.getItems()) {
                if (person.getName().equalsIgnoreCase(command.getSecondCommands().get(0))) {
                    // This is our man
                    won = ((Character) person).startConversation();
                }
            }
        }
        if (won)
            won();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    public void quit(Command command) 
    {
        if (command.hasSecondWords()) {
            Output.println("Quit what?");
        }
        else {
            playing = false;  // signal that we want to quit
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    public void won() {
        Image image = new ImageIcon("nedo.jpg").getImage();
        canvas.drawImage(image, WIDTH / 2 - 306, HEIGHT / 2 - 306);
        Output.println("Congratulations, you won!");
        playing = false;

        for (int i = 0; ; i++) {
            canvas.wait((int)(1 + Math.sin(Math.toRadians(i % 360)) / 2 * 100));
            canvas.setForegroundColor(new Color(40, 40, (i % 255)));
            canvas.setBackgroundColor(new Color(255 - (i % 255), (i + 40) % 255, (i % 255)));
            canvas.setFont(new Font("Arial", 1, 100));
            if (i % 2 == 0)
                canvas.drawString("You WON!!!", 0, HEIGHT / 2);
            else 
                canvas.eraseString("You WON!!!", 0, HEIGHT / 2);
        }
    }

    /**
     * Updates all rooms with their positions relative to a given room with coordinates given.
     */
    public void calculateRoomCoordinates(Room startRoom) {
        startRoom.setCoordinates((WIDTH - ROOM_WIDTH) / 2, (HEIGHT - ROOM_HEIGHT) / 2);
        calculateRoomCoordinates(startRoom, new ArrayList<Room>());
    }

    private void calculateRoomCoordinates(Room actualRoom, ArrayList<Room> roomsTested) {
        if (roomsTested.contains(actualRoom))
            return;

        roomsTested.add(actualRoom);
        HashMap<String, Room> roomsNearby = actualRoom.getExits();

        for (Map.Entry<String, Room> entry : roomsNearby.entrySet()) {
            String direction = entry.getKey();
            Room room = entry.getValue();
            int x = actualRoom.getXPos();
            int y = actualRoom.getYPos();

            if (roomsTested.contains(room))
                continue;

            if (direction.equals("north"))
                y -= ROOM_HEIGHT + ROOM_MARGIN;
            else if (direction.equals("west"))
                x -= ROOM_WIDTH + ROOM_MARGIN;
            else if (direction.equals("south"))
                y += ROOM_HEIGHT + ROOM_MARGIN;
            else if (direction.equals("east"))
                x += ROOM_WIDTH + ROOM_MARGIN;

            room.setCoordinates(x, y);

            calculateRoomCoordinates(room, roomsTested);
        }

        return;
    }

    /**
     * Gets path to go from one room to another. (Uses recursive help method)
     */

    public ArrayList<String> getPath(Room fromRoom, Room toRoom) {
        return getPathPrivate(fromRoom, toRoom, new ArrayList<String>(), new ArrayList<Room>());
    }

    private ArrayList<String> getPathPrivate(Room fromRoom, Room toRoom, ArrayList<String> pathWalked, ArrayList<Room> roomsTested) {
        if (fromRoom == toRoom) {
            ArrayList<String> array = new ArrayList<String>();
            return pathWalked;
        }

        roomsTested.add(fromRoom);

        HashMap<String, Room> roomsNearby = fromRoom.getExits();

        for (Map.Entry<String, Room> entry : roomsNearby.entrySet()) {
            String direction = entry.getKey();
            Room room = entry.getValue();
            if (!roomsTested.contains(room) && getPathPrivate(room, toRoom, pathWalked, roomsTested) != null) {
                pathWalked.add(direction);
                return pathWalked;
            }
        }

        return null;
    }

    private boolean nowRoomIsConnectedToDestinationRoom(ArrayList<Room> roomsTested, Room nowRoom, Room destinationRoom) {
        roomsTested.add(nowRoom);

        if (nowRoom == destinationRoom)
            return true;

        HashMap<String, Room> roomsNearby = nowRoom.getExits();
        for(Room room : roomsNearby.values()) {
            if (!roomsTested.contains(room) && nowRoomIsConnectedToDestinationRoom(roomsTested, room, destinationRoom))
                return true;
        }

        return false;
    }

    private String inverseDirection(String direction) {
        String inverseDirection = "";
        if (direction.equals("north"))
            inverseDirection = "south";
        else if (direction.equals("west"))
            inverseDirection = "east";
        else if (direction.equals("south"))
            inverseDirection = "north";
        else if (direction.equals("east"))
            inverseDirection = "west";

        return inverseDirection;
    }
}
